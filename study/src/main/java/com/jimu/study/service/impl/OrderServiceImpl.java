package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.enums.FolderEnum;
import com.jimu.study.mapper.OrderMapper;
import com.jimu.study.model.Orders;
import com.jimu.study.service.OrderService;
import com.jimu.study.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hxt
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private CourseFolderServiceImpl folderService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Integer insertOrder(Orders orders) {
        baseMapper.insert(orders);
        return orders.getOrderId();
    }

    @Override
    public Integer cancelOrder(Integer orderId) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setIsCancel(true);
        return baseMapper.updateById(orders);
    }

    @Override
    public Integer auditOrder(Integer orderId) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setIsAudit(true);
        return baseMapper.updateById(orders);
    }

    @Override
    public List<Orders> findOrderList(QueryWrapper<Orders> qw) {
        return baseMapper.selectList(qw);
    }

    @Override
    public Integer deleteOrder(Integer orderId) {
        return baseMapper.deleteById(orderId);
    }

    @Override
    @Transactional
    public Integer payOrder(String orderNo) {
        Orders orders = baseMapper.selectByNo(orderNo);
        orders.setIsPay(true);
        baseMapper.updateById(orders);
        folderService.insertFolder(orders.getUsersId(), orders.getCourseId(), FolderEnum.BUIED.getType());
        //暂时返回1
        return 1;
    }

    @Override
    public Orders findOneOrder(Integer orderId) {
        return baseMapper.selectById(orderId);
    }
}
