package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.enums.OrderStatusEnum;
import com.jimu.study.model.*;
import com.jimu.study.model.vo.OrdersVO;
import com.jimu.study.model.vo.VipOrderVO;
import com.jimu.study.service.*;
import com.jimu.study.utils.ListCopyUtil;
import com.jimu.study.utils.PasswordUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private RedisUtil redisUtil;

    @ApiOperation(value = "创建课程订单信息", notes = "isCreate=0为预览，不等于0为创建")
    @PostMapping("/createOrder")
    public OrdersVO createOrder(@RequestParam("courseId") Integer courseId,
                              @RequestParam("isCreate") Integer isCreate){
        Course course = courseService.findOneCourse(courseId);
        String free = "0.00";
        if(free.equals(course.getCoursePrice().toString())){
            //TODO 返回错误信息
            return null;
        }
        Orders orders = new Orders();
        orders.setCourseId(courseId);
        orders.setOrderNo(PasswordUtil.orderNum());
        orders.setUsersId((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()));
        orders.setOrderPrice(course.getCoursePrice());
        OrdersVO ordersVO = new OrdersVO();
        BeanUtils.copyProperties(orders, ordersVO);
        ordersVO.setOrderName("购买商品【" + course.getCourseName() + "】");
        if(isCreate != 0){
            Integer orderId = orderService.insertOrder(orders);
            ordersVO.setOrderId(orderId);
        }
        return ordersVO;
    }

    @ApiOperation(value = "返回课程订单列表", notes = "1为已付款，2为已取消，3为未核销，4为未付款")
    @GetMapping("/findOrders")
    public List<OrdersVO> findAllOrders(@RequestParam("condition") Integer condition){
        QueryWrapper<Orders> qw = new QueryWrapper<>();
        qw.eq("users_id", redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()));
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
        return ordersVos;
    }

    @ApiOperation("付款接口")
    @PostMapping("/payForCourse")
    public String payForCourse(@RequestParam("orderId") Integer orderId){
        //TODO 付款操作
        orderService.payOrder(orderId);
        return "付款成功";
    }

    @ApiOperation("取消课程订单")
    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") Integer orderId){
        Orders orders = orderService.findOneOrder(orderId);
        if(orders.getIsPay()){
            return "已付款，无法取消，不接受退款";
        }
        try {
            orderService.cancelOrder(orderId);
            return "成功";
        }catch (Exception e){
            return "取消订单失败";
        }
    }

    @ApiOperation("核销课程订单")
    @PostMapping("/auditOrder")
    public String auditOrder(@RequestParam("orderId") Integer orderId){
        Orders orders = orderService.findOneOrder(orderId);
        if(!orders.getIsPay()){
            return "未付款，不能核销，请先付款f";
        }
        try {
            orderService.auditOrder(orderId);
            return "成功";
        }catch (Exception e){
            return "核销订单失败";
        }
    }

    @ApiOperation("删除课程订单")
    @DeleteMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("orderId") Integer orderId){
        try {
            orderService.deleteOrder(orderId);
            return "成功";
        }catch (Exception e){
            return "删除订单失败";
        }
    }

    @ApiOperation("返回VIP订单列表")
    @GetMapping("/findVipOrder")
    public List<VipOrderVO> findVipOrder(){
        try {
            List<VipOrder> vipOrders = vipOrderService.findVipOrderByUsersId((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()));
            List<VipOrderVO> vipOrderVos = new ArrayList<>();
            for(VipOrder vipOrder: vipOrders){
                VipType vipType = vipTypeService.findVipById(vipOrder.getVipId());
                VipOrderVO vipOrderVO = new VipOrderVO();
                vipOrderVO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vipOrder.getCreateTime()));
                vipOrderVO.setVipName(vipType.getVipName());
                vipOrderVO.setVipPrice(vipType.getVipPrice());
                vipOrderVos.add(vipOrderVO);
            }
            return vipOrderVos;
        }catch (NullPointerException e){
            return null;
        }
    }

    @ApiOperation("返回VIP订单详情")
    @GetMapping("/createVipOrder")
    public OrdersVO createVipOrder(@RequestParam("vipId") Integer vipId){
        VipType vipType = vipTypeService.findVipById(vipId);
        OrdersVO ordersVO = new OrdersVO();
        ordersVO.setOrderName("购买商品【" + vipType.getVipName() + "】");
        ordersVO.setOrderNo(PasswordUtil.orderNum());
        ordersVO.setOrderPrice(vipType.getVipPrice());
        ordersVO.setCreateTime(new Date());
        return ordersVO;
    }

    @ApiOperation("购买vip")
    @PostMapping("/buyVip")
    public void buyVip(@RequestParam("vipId") Integer vipId){
        Integer usersId = (Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString());
        //TODO 支付
        //判断是否付款成功
        if(true){
            VipType vipType = vipTypeService.findVipById(vipId);
            Users users = new Users();
            users.setUsersId(usersId);
            users.setVipId(vipId);
            Date date = new Date(System.currentTimeMillis() + vipType.getVipTime()*24*60*60*1000);
            users.setUsersVip(date);
            usersService.updateUsers(users);
            vipOrderService.insert(vipId, usersId);
            //TODO 需要返回一个值
        }
    }
}
