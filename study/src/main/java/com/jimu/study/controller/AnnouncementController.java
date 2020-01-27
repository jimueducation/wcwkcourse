package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jimu.study.common.HttpResult;
import com.jimu.study.model.Announcement;
import com.jimu.study.model.Users;
import com.jimu.study.service.AnnouncementService;
import com.jimu.study.service.UsersService;
import com.jimu.study.utils.JwtUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/anno")
public class AnnouncementController {

    @Autowired
    private AnnouncementService annoService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("创建公告信息")
    @PostMapping("/createAnno")
    @RequiresAuthentication
    public HttpResult<Object> createAnno(@RequestBody Announcement anno,
                                         HttpServletRequest request) {
        String token = request.getHeader("cok");
        Users users = usersService.getById((Integer) redisUtil.get(JwtUtil.getUsername(token)));
        //TODO 判断权限
        if (true) {
            anno.setEditor(users.getUsersNick());
            annoService.save(anno);
//            annoService.insertAnno(anno);
            return HttpResult.ok();
        }
        return HttpResult.ok("权限不足");
    }

    @ApiOperation("返回公告列表(分页)")
    @GetMapping("/annoList")
    public HttpResult<List<Announcement>> annoList(@RequestParam("pageSize") Integer pageSize,
                                       @RequestParam("pageCurrent") Integer pageCurrent) {
        Page<Announcement> page = new Page<>(pageCurrent, pageSize);
        return HttpResult.ok(annoService.findAnnoList(page).getRecords());
    }

    @ApiOperation("返回单个公告详情")
    @GetMapping("/getOneAnno")
    @Transactional
    public HttpResult<Announcement> getOneAnno(@RequestParam("annoId") Integer annoId) {
//        annoService.addReadNum(annoId);
        Announcement anno = annoService.getById(annoId);
        anno.setReadNum(anno.getReadNum() + 1);
        QueryWrapper<Announcement> qw = new QueryWrapper<>();
        annoService.update(anno, qw);
        return HttpResult.ok(anno);
    }
}
