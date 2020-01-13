package com.jimu.study.enums;

import lombok.Getter;

/**
 * @author hxt
 */
@Getter
public enum OrderStatusEnum {
    /**已付款*/
    IS_PAY(1),
    /**已取消*/
    IS_CANCEL(2),
    /**未核销*/
    NO_AUDIT(3),
    /**未付款*/
    NO_PAY(4)
    ;

    private Integer status;

    OrderStatusEnum(Integer status) {
        this.status = status;
    }
}
