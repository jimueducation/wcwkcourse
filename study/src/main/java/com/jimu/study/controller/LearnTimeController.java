package com.jimu.study.controller;

import com.jimu.study.model.LearnTime;
import com.jimu.study.service.LearnTimeService;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/learnTime")
public class LearnTimeController {

    @Autowired
    private LearnTimeService learnTimeService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("返回登录用户的学习时间")
    @GetMapping("/getTime")
    public LearnTime getTime(){
        Integer usersId = (Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString());
        return learnTimeService.findTime(usersId);
    }

    @ApiOperation(value = "修改学习时间", notes = "1为图文，2为视频，3为音频，时间单位分钟")
    @PostMapping("/updateTime")
    public void updateTime(@RequestParam("time") Integer time,
                           @RequestParam("timeType") Integer type){
        learnTimeService.updateTime(time, type);
    }
}
