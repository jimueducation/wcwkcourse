package com.jimu.study.controller;

import com.jimu.study.common.HttpResult;
import com.jimu.study.model.LearnTime;
import com.jimu.study.service.LearnTimeService;
import com.jimu.study.utils.JwtUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    @RequiresAuthentication
    public HttpResult<LearnTime> getTime(HttpServletRequest request) {
        String token = request.getHeader("cok");
        Integer usersId = (Integer) redisUtil.get(JwtUtil.getUsername(token));
        return HttpResult.ok(learnTimeService.findTime(usersId));
    }

    @ApiOperation(value = "修改学习时间", notes = "1为图文，2为视频，3为音频，时间单位分钟")
    @PostMapping("/updateTime")

    public void updateTime(@RequestParam("time") Integer time,
                           @RequestParam("timeType") Integer type,
                           HttpServletRequest request) {
        String token = request.getHeader("cok");
        Integer usersId = (Integer) redisUtil.get(JwtUtil.getUsername(token));
        learnTimeService.updateTime(time, type, usersId);
    }
}
