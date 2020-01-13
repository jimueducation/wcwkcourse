package com.jimu.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.CourseContent;

import java.util.List;

/**
 * @author hxt
 */
public interface CourseContentService extends IService<CourseContent> {

    /**插入数据*/
    Integer insertCourseContent(CourseContent courseContent);

    /**修改数据*/
    Integer updateCourseContent(CourseContent courseContent);

    /**根据课程ID查找目录*/
    List<CourseContent> findContentByCourseId(Integer courseId);

    /**根据ID返回目录内容*/
    CourseContent findContentById(Integer contentId);
}
