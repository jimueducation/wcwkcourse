package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.LearnTimeMapper;
import com.jimu.study.model.LearnTime;
import com.jimu.study.service.LearnTimeService;
import com.jimu.study.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hxt
 */
@Service("learnTimeService")
public class LearnTimeServiceImpl extends ServiceImpl<LearnTimeMapper, LearnTime> implements LearnTimeService {

    private final int pic = 1;
    private final int video = 2;
    private final int audio = 3;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Boolean updateTime(Integer time, Integer type) {
        Integer usersId = (Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString());
        switch (type){
            case pic: return baseMapper.updatePicDay(time, usersId);
            case video: return baseMapper.updateVideoDay(time, usersId);
            case audio: return baseMapper.updateAudioDay(time, usersId);
        }
        return false;
    }

    @Override
    public LearnTime findTime(Integer usersId) {
        QueryWrapper<LearnTime> qw = new QueryWrapper<>();
        qw.eq("users_id", usersId);
        return baseMapper.selectOne(qw);
    }

    @Override
    public Boolean updateEveryDay() {
        return baseMapper.updateEveryDay();
    }
}
