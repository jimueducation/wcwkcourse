package com.jimu.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.VipType;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
public interface VipTypeService extends IService<VipType> {

    /**根据id查找对应的vip*/
    VipType findVipById(Integer vipId);

    /**添加数据*/
    Integer insert(VipType vipType);

    /**返回VIP列表*/
    List<VipType> findVipList();
}
