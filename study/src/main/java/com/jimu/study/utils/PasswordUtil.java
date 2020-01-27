package com.jimu.study.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author hxt
 */
public class PasswordUtil {

    public static String getSalt() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    public static String encode(String password, String salt) {
        return new Md5Hash(password, salt, 1).toHex();
    }

    public static String orderNum() {
        StringBuilder orderNum = new StringBuilder(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        Random random=new Random();
        for (int i = 0; i < 4; i++) {
            orderNum.append(random.nextInt(10));
        }
        return orderNum.toString();
    }
}
