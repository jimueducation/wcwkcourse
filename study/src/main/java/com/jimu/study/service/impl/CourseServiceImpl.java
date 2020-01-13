package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.CourseMapper;
import com.jimu.study.model.Course;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hxt
 */
@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public Integer insertCourse(Course course) {
        return baseMapper.insert(course);
    }

    @Override
    public Integer updateCourse(Course course) {
        return baseMapper.updateById(course);
    }

    @Override
    public IPage<Course> findCoursesPage(IPage<Course> page, QueryWrapper<Course> qw) {
        return baseMapper.selectPage(page, qw);
    }

    @Override
    public Course findOneCourse(Integer courseId) {
        return baseMapper.selectById(courseId);
    }

    @Override
    public List<CourseList> searchCourse(String search, IPage<CourseList> page) {
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.like("course_name", search);
        return baseMapper.searchCourse(qw, page);
    }

    @Override
    public List<CourseList> newestCourse() {
        Page<CourseList> page = new Page<>(1, 3);
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.orderByDesc("update_time");
        return baseMapper.searchCourse(qw, page);
    }

    @Override
    public List<CourseList> hotCourse(IPage<CourseList> page) {
        QueryWrapper<CourseList> qw = new QueryWrapper<>();
        qw.orderByDesc("study_num");
        return baseMapper.searchCourse(qw, page);
    }

    @Override
    public List<CourseList> priceUpCourse(IPage<CourseList> page) {
        QueryWrapper<CourseList> qw = new QueryWrapper<>();
        qw.orderByAsc("course_price");
        qw.gt("course_price", 0);
        return baseMapper.searchCourse(qw, page);
    }
}
