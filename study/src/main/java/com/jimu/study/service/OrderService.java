package com.jimu.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.Orders;

import java.util.List;

/**
 * @author hxt
 */
public interface OrderService extends IService<Orders> {

    /**创建订单*/
    Integer insertOrder(Orders orders);

    /**取消订单*/
    Integer cancelOrder(Integer orderId);

    /**核销订单*/
    Integer auditOrder(Integer orderId);

    /**根据条件查找订单*/
    List<Orders> findOrderList(QueryWrapper<Orders> qw);

    /**删除订单*/
    Integer deleteOrder(Integer orderId);

    /**付款*/
    Integer payOrder(String orderNo);

    /**返回一个订单信息*/
    Orders findOneOrder(Integer orderId);
}
