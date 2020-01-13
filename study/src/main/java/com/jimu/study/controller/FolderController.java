package com.jimu.study.controller;

import com.jimu.study.enums.FolderEnum;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.service.CourseFolderService;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<CourseList> footList(){
        return folderService.findCourseFolder((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()), FolderEnum.FOOT.getType());
    }

    @ApiOperation("返回收藏列表")
    @GetMapping("/favoritesList")
    public List<CourseList> favoritesList(){
        return folderService.findCourseFolder((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()), FolderEnum.FAVORITES.getType());
    }

    @ApiOperation("收藏一个课程")
    @PostMapping("/addFavorites")
    public Object addFavorites(@RequestParam("courseId") Integer courseId){
        if(folderService.insertFolder((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()), courseId, FolderEnum.FAVORITES.getType())){
            //TODO 返回一个值(待定)
            return "收藏成功";
        }else {
            //TODO 已经收藏过了，报个错
            return "不可重复收藏";
        }
    }

    @ApiOperation("取消收藏一个课程")
    @PostMapping("/deleteFavorites")
    public Object deleteFavoretes(@RequestParam("courseId") Integer courseId){
        folderService.deleteFavorites((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()), courseId);
        //TODO 返回一个值(待定)
        return "取消收藏成功";
    }
}
