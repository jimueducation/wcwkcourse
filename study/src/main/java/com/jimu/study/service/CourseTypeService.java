package com.jimu.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.CourseType;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
public interface CourseTypeService extends IService<CourseType> {

    /**插入课程分类数据*/
    Integer insertCourseType(CourseType courseType);

    /**修改课程分类*/
    Integer updateCourseType(CourseType courseType);

    /**查找所有分类*/
    List<Map<String, Object>> findAllCourseType(QueryWrapper<CourseType> qw);
}
