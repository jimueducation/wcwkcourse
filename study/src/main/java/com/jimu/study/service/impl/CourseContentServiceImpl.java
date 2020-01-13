package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.CourseContentMapper;
import com.jimu.study.model.Course;
import com.jimu.study.model.CourseContent;
import com.jimu.study.service.CourseContentService;
import com.jimu.study.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hxt
 */
@Service
public class CourseContentServiceImpl extends ServiceImpl<CourseContentMapper, CourseContent> implements CourseContentService {

    @Autowired
    private CourseService courseService;

    @Override
    @Transactional
    public Integer insertCourseContent(CourseContent courseContent) {
        Course course = courseService.findOneCourse(courseContent.getCourseId());
        course.setCourseNum(course.getCourseNum() + 1);
        courseService.updateCourse(course);
        return baseMapper.insert(courseContent);
    }

    @Override
    public Integer updateCourseContent(CourseContent courseContent) {
        return baseMapper.updateById(courseContent);
    }

    @Override
    public List<CourseContent> findContentByCourseId(Integer courseId) {
        QueryWrapper<CourseContent> qw = new QueryWrapper<>();
        qw.eq("course_id", courseId);
        return baseMapper.selectList(qw);
    }

    @Override
    public CourseContent findContentById(Integer contentId) {
        return baseMapper.selectById(contentId);
    }
}
