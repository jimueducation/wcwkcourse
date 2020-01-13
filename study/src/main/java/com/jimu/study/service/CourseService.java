package com.jimu.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.Course;
import com.jimu.study.model.vo.CourseList;

import java.util.List;

/**
 * @author hxt
 */
public interface CourseService extends IService<Course> {

    /**插入课程信息*/
    Integer insertCourse(Course course);

    /**编辑课程信息*/
    Integer updateCourse(Course course);

    /**分页*/
    IPage<Course> findCoursesPage(IPage<Course> page, QueryWrapper<Course> qw);

    /**根据ID精准查找课程信息*/
    Course findOneCourse(Integer courseId);

    /**课程搜索*/
    List<CourseList> searchCourse(String search, IPage<CourseList> page);

    /**返回最新课程*/
    List<CourseList> newestCourse();

    /**返回热门课程*/
    List<CourseList> hotCourse(IPage<CourseList> page);

    /**根据价格*/
    List<CourseList> priceUpCourse(IPage<CourseList> page);
}
