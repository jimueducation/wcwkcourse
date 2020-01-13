package com.jimu.study.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jimu.study.model.Announcement;
import com.jimu.study.model.Users;
import com.jimu.study.service.AnnouncementService;
import com.jimu.study.service.UsersService;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public String createAnno(@RequestBody Announcement anno){
        Users users = usersService.getById((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()));
        //TODO 判断权限
        if(true){
            anno.setEditor(users.getUsersNick());
            annoService.insertAnno(anno);
            return "成功";
        }
        return "失败";
    }

    @ApiOperation("返回公告列表(分页)")
    @GetMapping("/annoList")
    public List<Announcement> annoList(@RequestParam("pageSize") Integer pageSize,
                                       @RequestParam("pageCurrent") Integer pageCurrent){
        Page<Announcement> page = new Page<>(pageCurrent, pageSize);
        return annoService.findAnnoList(page).getRecords();
    }

    @ApiOperation("返回单个公告详情")
    @GetMapping("/getOneAnno")
    @Transactional
    public Announcement getOneAnno(@RequestParam("annoId") Integer annoId){
        annoService.addReadNum(annoId);
        return annoService.findOneAnno(annoId);
    }
}
