package com.jimu.study.service;

/**
 * @author hxt
 * @date 2020/1/15 11:25
 */
public interface WechatPayService {

    /**
     * 生成支付二维码URL
     * @param orderId 订单号ID
     * @param outTradeNo 商户订单号
     * @param signType 签名类型
     * @throws Exception
     */
    String wxPayUrl(Integer orderId, String outTradeNo, String signType) throws Exception;

}
