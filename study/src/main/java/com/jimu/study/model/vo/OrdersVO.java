package com.jimu.study.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hxt
 */
@Data
public class OrdersVO {

    @ApiModelProperty("订单ID")
    private Integer orderId;

    @ApiModelProperty("课程ID")
    private Integer courseId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("订单名称")
    private String orderName;

    @ApiModelProperty("订单的总价")
    private BigDecimal orderPrice;

    @ApiModelProperty("订单创建的时间")
    private Date createTime;

    @ApiModelProperty("是否付款")
    private Boolean isPay;

    public String getCreateTime(){
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
        }catch (NullPointerException e){
            return null;
        }
    }
}
