package com.jimu.study.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hxt
 */
@Data
public class VipOrderVO {

    private String vipName;

    private BigDecimal vipPrice;

    private String createTime;
}
