package com.jimu.study.controller;

import com.jimu.study.common.HttpResult;
import com.jimu.study.enums.FolderEnum;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.service.CourseFolderService;
import com.jimu.study.utils.JwtUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private CourseFolderService folderService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("返回足迹列表")
    @GetMapping("/footList")
    @RequiresAuthentication
    public HttpResult<List<CourseList>> footList(HttpServletRequest request) {
        String token = request.getHeader("cok");
        return HttpResult.ok(folderService.findCourseFolder((Integer) redisUtil.get(JwtUtil.getUsername(token)), FolderEnum.FOOT.getType()));
    }

    @ApiOperation("返回收藏列表")
    @GetMapping("/favoritesList")
    @RequiresAuthentication
    public HttpResult<List<CourseList>> favoritesList(HttpServletRequest request) {
        String token = request.getHeader("cok");
        return HttpResult.ok(folderService.findCourseFolder((Integer) redisUtil.get(JwtUtil.getUsername(token)), FolderEnum.FAVORITES.getType()));
    }

    @ApiOperation("收藏一个课程")
    @PostMapping("/addFavorites")
    @RequiresAuthentication
    public HttpResult<Object> addFavorites(@RequestParam("courseId") Integer courseId,
                                           HttpServletRequest request) {
        String token = request.getHeader("cok");
        if (folderService.insertFolder((Integer) redisUtil.get(JwtUtil.getUsername(token)), courseId, FolderEnum.FAVORITES.getType())) {
            return HttpResult.ok("收藏成功");
        } else {
            return HttpResult.error("不可重复收藏");
        }
    }

    @ApiOperation("取消收藏一个课程")
    @PostMapping("/deleteFavorites")
    @RequiresAuthentication
    public HttpResult<Object> deleteFavoretes(@RequestParam("courseId") Integer courseId,
                                              HttpServletRequest request) {
        String token = request.getHeader("cok");
        folderService.deleteFavorites((Integer) redisUtil.get(JwtUtil.getUsername(token)), courseId);
        return HttpResult.ok("取消收藏成功");
    }
}
