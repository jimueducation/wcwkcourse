package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hxt
 */
@Data
public class VipOrder {

    @TableId(value = "vip_order_id", type = IdType.AUTO)
    private Integer vipOrderId;

    @ApiModelProperty(value = "对应vip的ID", required = true)
    private Integer vipId;

    @ApiModelProperty(value = "对应购买用户的ID", required = true)
    private Integer usersId;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
