package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.VipTypeMapper;
import com.jimu.study.model.VipType;
import com.jimu.study.service.VipTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author hxt
 */
@Service
public class VipTypeServiceImpl extends ServiceImpl<VipTypeMapper, VipType> implements VipTypeService {
    @Override
    public VipType findVipById(Integer vipId) {
        QueryWrapper<VipType> qw = new QueryWrapper<>();
        qw.eq("vip_id", vipId);
        return baseMapper.selectById(vipId);
    }

    @Override
    public Integer insert(VipType vipType) {
        return baseMapper.insert(vipType);
    }

    @Override
    public List<VipType> findVipList() {
        return baseMapper.selectList(new QueryWrapper<>());
    }
}
