package com.jimu.study.controller;

import com.jimu.study.common.WechatConstants;
import com.jimu.study.service.WechatPayService;
import com.jimu.study.utils.PasswordUtil;
import com.jimu.study.utils.WechatPayUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hxt
 * @date 2020/1/15 9:57
 */
@RestController
@RequestMapping("/wechat")
public class WechatPayController {

    @Autowired
    private WechatPayService wechatPayService;

    final private String signType = WechatConstants.SING_MD5;

    @ApiOperation(value = "微信支付调用接口", notes = "返回值为一张图片")
    @GetMapping("/pay")
    public void payUrl(HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestParam(value = "orderId") Integer orderId) throws Exception {
        WechatPayUtil.writerPayImage(response, wechatPayService.wxPayUrl(orderId, PasswordUtil.orderNum(), signType));
    }
}
