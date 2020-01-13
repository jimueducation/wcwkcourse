package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.model.CourseType;
import com.jimu.study.service.CourseTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/courseType")
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    @ApiOperation("查找课程分类")
    @GetMapping("/findAllCourseType")
    public List<Map<String, Object>> findAllCourseType(){
        QueryWrapper<CourseType> qw = new QueryWrapper<>();
        return courseTypeService.findAllCourseType(qw);
    }
}
