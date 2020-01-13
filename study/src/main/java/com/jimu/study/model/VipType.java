package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hxt
 */
@Data
public class VipType {

    @TableId(value = "vip_id", type = IdType.AUTO)
    private Integer vipId;

    @ApiModelProperty(value = "vip名字", required = true)
    private String vipName;

    @ApiModelProperty("vip区分号")
    private Integer vipType;

    @ApiModelProperty(value = "vip价格", required = true)
    private BigDecimal vipPrice;

    @ApiModelProperty(value = "vip有效时间", required = true)
    private Integer vipTime;

}
