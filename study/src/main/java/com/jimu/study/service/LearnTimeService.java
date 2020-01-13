package com.jimu.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.LearnTime;

/**
 * @author hxt
 */
public interface LearnTimeService extends IService<LearnTime> {

    /**修改时间*/
    Boolean updateTime(Integer time, Integer type);

    /**返回时间*/
    LearnTime findTime(Integer usersId);

    /**每天更新时间*/
    Boolean updateEveryDay();
}
