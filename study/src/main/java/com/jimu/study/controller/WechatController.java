package com.jimu.study.controller;

import com.jimu.study.common.WechatConstants;
import com.jimu.study.service.WechatPayService;
import com.jimu.study.utils.WechatUtil;
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
public class WechatController {

    @Autowired
    private WechatPayService wechatPayService;

    final private String signType = WechatConstants.SING_MD5;

    @ApiOperation(value = "微信支付调用接口", notes = "返回值为一张图片")
    @GetMapping("/pay")
    public void payUrl(HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestParam(value = "orderId") Integer orderId) throws Exception {
        WechatUtil.writerPayImage(response, wechatPayService.wxPayUrl(orderId, signType));
    }

    @ApiOperation(value = "微信登录二维码", notes = "返回值为一张图片")
    @GetMapping("/login")
    public void loginUrl(HttpServletRequest request, HttpServletResponse response){
        try {
            WechatUtil.writerPayImage(response, "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0565484d8fa3a4fc&redirect_uri=http://wchatsell.nat300.top/users/wechatLogin&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
