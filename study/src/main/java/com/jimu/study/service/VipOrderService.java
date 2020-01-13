package com.jimu.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.VipOrder;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
public interface VipOrderService extends IService<VipOrder> {

    /**创建订单*/
    Integer insert(Integer vipId, Integer usersId);

    /**根据用户ID查找订单*/
    List<VipOrder> findVipOrderByUsersId(Integer usersId);
}
