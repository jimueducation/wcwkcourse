package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hxt
 */
@Data
public class Orders {

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty(value = "购买的用户ID", required = true)
    private Integer usersId;

    @ApiModelProperty(value = "购买的课程ID", required = true)
    private Integer courseId;

    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "订单总价", required = true)
    private BigDecimal orderPrice;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否付款", required = true)
    private Boolean isPay;

    @ApiModelProperty(value = "是否取消", required = true)
    private Boolean isCancel;

    @ApiModelProperty(value = "是否核销", required = true)
    private Boolean isAudit;
}
