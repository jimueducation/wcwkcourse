package com.jimu.study.service.impl;

import com.jimu.study.common.HttpsClient;
import com.jimu.study.common.WechatConstants;
import com.jimu.study.common.WechatPay;
import com.jimu.study.model.Course;
import com.jimu.study.model.Orders;
import com.jimu.study.service.CourseService;
import com.jimu.study.service.OrderService;
import com.jimu.study.service.WechatPayService;
import com.jimu.study.utils.PasswordUtil;
import com.jimu.study.utils.RedisUtil;
import com.jimu.study.utils.WechatPayUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hxt
 * @date 2020/1/15 11:26
 */
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CourseService courseService;

    @Override
    public String wxPayUrl(Integer orderId, String signType) throws Exception {
        Orders orders = orderService.findOneOrder(orderId);
        String outTradeNo = orders.getOrderNo();
        Course course = courseService.findOneCourse(orders.getCourseId());
        HashMap<String, String> data = new HashMap<String, String>();
        //公众账号ID
        data.put("appid", WechatPay.APP_ID);
        //商户号
        data.put("mch_id", WechatPay.MCH_ID);
        //随机字符串
        data.put("nonce_str", WechatPayUtil.getNonceStr());
        //商品描述
        data.put("body", "购买商品【" + course.getCourseName() + "】");
        //商户订单号
        data.put("out_trade_no", outTradeNo);
        //标价币种
        data.put("fee_type","CNY");
        //标价金额
        data.put("total_fee",String.valueOf(Math.round(orders.getOrderPrice().floatValue() * 100)));
        //用户的IP
        data.put("spbill_create_ip", WechatPay.CREATE_IP);
        //通知地址
        data.put("notify_url", WechatPay.NOTIFY_URL);
        //交易类型
        data.put("trade_type","NATIVE");
        //签名类型
        data.put("sign_type",signType);
        //签名
        data.put("sign", WechatPayUtil.getSignature(data, WechatPay.API_KEY, signType));

        String requestXML = WechatPayUtil.mapToXml(data);
        String reponseString = HttpsClient.httpsRequestReturnString(WechatConstants.PAY_UNIFIEDORDER, HttpsClient.METHOD_POST, requestXML);
        Map<String, String> resultMap = WechatPayUtil.processResponseXml(reponseString, signType);
        if (resultMap.get(WechatConstants.RETURN_CODE).equals("SUCCESS")) {
            if (resultMap.get("code_url").isEmpty()) {
                redisUtil.set(outTradeNo, orderId);
            }
            return resultMap.get("code_url");
        }
        return null;
    }
}
