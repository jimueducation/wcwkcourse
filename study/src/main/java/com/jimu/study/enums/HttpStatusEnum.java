package com.jimu.study.enums;

import lombok.Getter;

/**
 * @author hxt
 * @date 2020/1/14 8:40
 */
@Getter
public enum HttpStatusEnum {
    /**状态码正常*/
    SC_OK(200),
    /**404客户端错误*/
    SC_NOT_FOUND(404),
    /**500服务器错误*/
    SC_INTERNAL_SERVER_ERROR(500)
    ;

    private int code;

    HttpStatusEnum(int code) {
        this.code = code;
    }
}
