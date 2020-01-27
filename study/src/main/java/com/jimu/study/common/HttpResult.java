package com.jimu.study.common;

import com.jimu.study.enums.HttpStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author hxt
 * @date 2020/1/14 8:36
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult<T> {

    private int code = 200;
    private String msg;
    private T data;

    public HttpResult(int code) {
        this.code = code;
    }
    public HttpResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HttpResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> HttpResult<T> ok() { return new HttpResult<T>(HttpStatusEnum.SC_OK.getCode()); }

    public static <T> HttpResult<T> ok(String msg) {
        return new HttpResult<>(HttpStatusEnum.SC_OK.getCode(), msg);
    }

    public static <T> HttpResult<T> ok(T data) {
        return new HttpResult<>(HttpStatusEnum.SC_OK.getCode(), data);
    }

    public static <T> HttpResult<T> ok(String msg, T data) {
        return new HttpResult<>(HttpStatusEnum.SC_OK.getCode(), msg, data);
    }

    public static <T> HttpResult<T> error() {
        return new HttpResult<>(HttpStatusEnum.SC_INTERNAL_SERVER_ERROR.getCode());
    }

    public static <T> HttpResult<T> error(String msg) {
        return new HttpResult<>(HttpStatusEnum.SC_INTERNAL_SERVER_ERROR.getCode(), msg);
    }

    public static <T> HttpResult<T> error(int code, String msg, T data) {
        return new HttpResult<>(code, msg, data);
    }

    public static <T> HttpResult<T> error(int code, String msg) {
        return new HttpResult<>(code, msg);
    }

    public static <T> HttpResult<T> notFound() {
        return new HttpResult<>(HttpStatusEnum.SC_NOT_FOUND.getCode(), "Dreams can't stolen but page can");
    }
}
