package com.jimu.study.common;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author hxt
 * @date 2020/1/15 9:49
 */
public class WechatPay {

    /** 微信开发平台应用ID（公众号ID） wxab8acb865bb1637e */
    public static final String APP_ID = "wx0facbef237c3cd62";

    /** 商户号（商户号ID） 11473623 */
    public static final String MCH_ID = "1416397002";

    /** API key（商户号里面的） 2ab9071b06b9f739b950ddb41db2690d */
    public static final String API_KEY = "JMKJ6789655udfsdfiREUIYGCHSRRFJM";

    /** 发起支付的Ip */
    public static final String CREATE_IP = "127.0.0.1";

    /** 回调地址（需要外网可以访问的IP） */
    public static final String  NOTIFY_URL = "server.natappfree.cc:46006/order/payForCourse";

    /** 微信统一下单接口 */
    public static final String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /** 应用对应的凭证（在公众号里面） 86ae4a77893342f7568947e243c84d9aa */
    public static final String APP_SECRET = "ef079b992f911ebb85faf9fae9a46025";

    public static String getIP(HttpServletRequest request){
        String ip = request.getRemoteAddr();
        return ip;
    }
}
