package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.VipOrderMapper;
import com.jimu.study.model.VipOrder;
import com.jimu.study.service.VipOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author hxt
 */
@Service
public class VipOrderServiceImpl extends ServiceImpl<VipOrderMapper, VipOrder> implements VipOrderService {

    @Override
    public Integer insert(Integer vipId, Integer usersId) {
        VipOrder vipOrder = new VipOrder();
        vipOrder.setUsersId(usersId);
        vipOrder.setVipId(vipId);
        return baseMapper.insert(vipOrder);
    }

    @Override
    public List<VipOrder> findVipOrderByUsersId(Integer usersId) {
        QueryWrapper<VipOrder> qw = new QueryWrapper<>();
        qw.eq("users_id", usersId);
        return baseMapper.selectList(qw);
    }
}
