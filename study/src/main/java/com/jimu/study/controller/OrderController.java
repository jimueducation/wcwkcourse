package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.common.HttpResult;
import com.jimu.study.common.WechatConstants;
import com.jimu.study.common.WechatPay;
import com.jimu.study.enums.OrderStatusEnum;
import com.jimu.study.model.*;
import com.jimu.study.model.vo.OrdersVO;
import com.jimu.study.model.vo.VipOrderVO;
import com.jimu.study.service.*;
import com.jimu.study.utils.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private VipOrderService vipOrderService;

    @Autowired
    private VipTypeService vipTypeService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CourseFolderService folderService;

    @Autowired
    private RedisUtil redisUtil;

    final private String signType = WechatConstants.SING_MD5;

    @ApiOperation(value = "创建课程订单信息", notes = "isCreate=0为预览，不等于0为创建")
    @PostMapping("/createOrder")
    @RequiresAuthentication
    public HttpResult<OrdersVO> createOrder(@RequestParam("courseId") Integer courseId,
                                           @RequestParam("isCreate") Integer isCreate,
                                            HttpServletRequest request) {
        String token = request.getHeader("cok");
        Course course = courseService.findOneCourse(courseId);
        String free = "0.00";
        if (free.equals(course.getCoursePrice().toString())) {
            return HttpResult.error("课程为免费，不用购买");
        } else if (folderService.isBuiedCourse((Integer) redisUtil.get(JwtUtil.getUsername(token)), courseId) != 0) {
            return HttpResult.error("已购买该课程");
        }
        Orders orders = new Orders();
        orders.setCourseId(courseId);
        orders.setOrderNo(PasswordUtil.orderNum());
        orders.setUsersId((Integer) redisUtil.get(JwtUtil.getUsername(token)));
        orders.setOrderPrice(course.getCoursePrice());
        OrdersVO ordersVO = new OrdersVO();
        BeanUtils.copyProperties(orders, ordersVO);
        ordersVO.setOrderName("购买商品【" + course.getCourseName() + "】");
        if (isCreate != 0) {
            Integer orderId = orderService.insertOrder(orders);
            ordersVO.setOrderId(orderId);
        }
        return HttpResult.ok(ordersVO);
    }

    @ApiOperation(value = "返回课程订单列表", notes = "1为已付款，2为已取消，3为未核销，4为未付款")
    @GetMapping("/findOrders")
    @RequiresAuthentication
    public HttpResult<List<OrdersVO>> findAllOrders(@RequestParam("condition") Integer condition,
                                                    HttpServletRequest request) {
        String token = request.getHeader("cok");
        QueryWrapper<Orders> qw = new QueryWrapper<>();
        qw.eq("users_id", redisUtil.get(JwtUtil.getUsername(token)));
        qw.eq(OrderStatusEnum.NO_PAY.getStatus().equals(condition), "is_pay", false);
        qw.eq(OrderStatusEnum.IS_PAY.getStatus().equals(condition), "is_pay", true);
        qw.eq(OrderStatusEnum.IS_CANCEL.getStatus().equals(condition), "is_cancel", true);
        qw.eq(OrderStatusEnum.NO_AUDIT.getStatus().equals(condition), "is_audit", false);
        qw.orderByDesc("create_time");
        List<Orders> orders = orderService.findOrderList(qw);
        List<OrdersVO> ordersVos = ListCopyUtil.copyOrderListToVo(orders);
        ordersVos.forEach(obj -> {
            Course course = courseService.findOneCourse(obj.getCourseId());
            obj.setOrderName("购买商品【" + course.getCourseName() + "】");
        });
        return HttpResult.ok(ordersVos);
    }

    @ApiOperation("付款接口")
    @RequestMapping("/payForCourse")
    public HttpResult<Object> payForCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //TODO 付款操作
        Integer bool = 0;
        //商户订单号
        String outTradeNo = null;
        String xmlContent = "<xml>" +
                "<return_code><![CDATA[FAIL]]></return_code>" +
                "<return_msg><![CDATA[签名失败]]></return_msg>" +
                "</xml>";
        try {
            String requstXml = WechatUtil.getStreamString(request.getInputStream());
            System.out.println("requstXml : " + requstXml);
            Map<String,String> map = WechatUtil.xmlToMap(requstXml);
            String returnCode = map.get(WechatConstants.RETURN_CODE);
            //校验一下
            if (StringUtils.isNotBlank(returnCode) && StringUtils.equals(returnCode, "SUCCESS") && WechatUtil.isSignatureValid(map, WechatPay.API_KEY, signType)) {
                //商户订单号
                outTradeNo = map.get("out_trade_no");
                System.out.println("outTradeNo : "+ outTradeNo);
                //微信支付订单号
                String transactionId = map.get("transaction_id");
                System.out.println("transactionId : "+ transactionId);
                //支付完成时间
                SimpleDateFormat payFormat= new SimpleDateFormat("yyyyMMddHHmmss");
                Date payDate = payFormat.parse(map.get("time_end"));

                SimpleDateFormat systemFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("支付时间：" + systemFormat.format(payDate));
                Integer orderId = (Integer) redisUtil.get(outTradeNo);
                //TODO payOrder获取不到usersId
                bool = orderService.payOrder(outTradeNo);
                redisUtil.del(outTradeNo);
                xmlContent = "<xml>" +
                        "<return_code><![CDATA[SUCCESS]]></return_code>" +
                        "<return_msg><![CDATA[OK]]></return_msg>" +
                        "</xml>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WechatUtil.responsePrint(response, xmlContent);
        if (bool == 1) {
            return HttpResult.ok("付款成功");
        } else {
            return HttpResult.error("付款失败");
        }
    }

    @ApiOperation("取消课程订单")
    @PostMapping("/cancelOrder")
    @RequiresAuthentication
    public HttpResult<Object> cancelOrder(@RequestParam("orderId") Integer orderId) {
        Orders orders = orderService.findOneOrder(orderId);
        if (orders.getIsPay()) {
            return HttpResult.error("已付款，无法取消，不接受退款");
        }
        try {
            orderService.cancelOrder(orderId);
            return HttpResult.ok("成功");
        } catch (Exception e) {
            return HttpResult.error("取消订单失败");
        }
    }

    @ApiOperation("核销课程订单")
    @PostMapping("/auditOrder")
    @RequiresAuthentication
    public HttpResult<Object> auditOrder(@RequestParam("orderId") Integer orderId) {
        Orders orders = orderService.findOneOrder(orderId);
        if (!orders.getIsPay()) {
            return HttpResult.error("未付款，不能核销，请先付款");
        }
        try {
            orderService.auditOrder(orderId);
            return HttpResult.ok("成功");
        } catch (Exception e) {
            return HttpResult.error("核销订单失败");
        }
    }

    @ApiOperation("删除课程订单")
    @DeleteMapping("/deleteOrder")
    @RequiresAuthentication
    public HttpResult<Object> deleteOrder(@RequestParam("orderId") Integer orderId) {
        try {
            orderService.deleteOrder(orderId);
            return HttpResult.ok("成功");
        } catch (Exception e) {
            return HttpResult.error("删除订单失败");
        }
    }

    @ApiOperation("返回VIP订单列表")
    @GetMapping("/findVipOrder")
    @RequiresAuthentication
    public HttpResult<List<VipOrderVO>> findVipOrder(HttpServletRequest request) {
        String token = request.getHeader("cok");
        try {
            List<VipOrder> vipOrders = vipOrderService.findVipOrderByUsersId((Integer) redisUtil.get(JwtUtil.getUsername(token)));
            List<VipOrderVO> vipOrderVos = new ArrayList<>();
            for (VipOrder vipOrder: vipOrders) {
                VipType vipType = vipTypeService.findVipById(vipOrder.getVipId());
                VipOrderVO vipOrderVO = new VipOrderVO();
                vipOrderVO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vipOrder.getCreateTime()));
                vipOrderVO.setVipName(vipType.getVipName());
                vipOrderVO.setVipPrice(vipType.getVipPrice());
                vipOrderVos.add(vipOrderVO);
            }
            return HttpResult.ok(vipOrderVos);
        } catch (NullPointerException e) {
            return HttpResult.error("空指针");
        }
    }

    @ApiOperation("返回VIP订单详情")
    @GetMapping("/createVipOrder")
    @RequiresAuthentication
    public HttpResult<OrdersVO> createVipOrder(@RequestParam("vipId") Integer vipId) {
        VipType vipType = vipTypeService.findVipById(vipId);
        OrdersVO ordersVO = new OrdersVO();
        ordersVO.setOrderName("购买商品【" + vipType.getVipName() + "】");
        ordersVO.setOrderNo(PasswordUtil.orderNum());
        ordersVO.setOrderPrice(vipType.getVipPrice());
        ordersVO.setCreateTime(new Date());
        return HttpResult.ok(ordersVO);
    }

    @ApiOperation("购买vip")
    @PostMapping("/buyVip")
    @RequiresAuthentication
    public HttpResult<Object> buyVip(@RequestParam("vipId") Integer vipId,
                                     HttpServletRequest request) {
        String token = request.getHeader("cok");
        Integer usersId = (Integer) redisUtil.get(JwtUtil.getUsername(token));
        //TODO 支付
        //判断是否付款成功
        if (true) {
            VipType vipType = vipTypeService.findVipById(vipId);
            Users users = new Users();
            users.setUsersId(usersId);
            users.setVipId(vipId);
            Date date = new Date(System.currentTimeMillis() + vipType.getVipTime() * 24 * 60 * 60 * 1000);
            users.setUsersVip(date);
            usersService.updateUsers(users);
            vipOrderService.insert(vipId, usersId);
            return HttpResult.ok();
        }
        return HttpResult.ok();
    }
}
