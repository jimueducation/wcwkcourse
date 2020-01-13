package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jimu.study.model.Course;
import com.jimu.study.model.Teacher;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.model.vo.TeacherVO;
import com.jimu.study.service.CourseFolderService;
import com.jimu.study.service.CourseService;
import com.jimu.study.service.TeacherService;
import com.jimu.study.utils.ListCopyUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseFolderService folderService;

    @ApiOperation("根据ID返回讲师详细信息")
    @GetMapping("/findTeacherById")
    public TeacherVO findTeacherById(@RequestParam("teacherId") Integer teacherId){
        Teacher teacher = teacherService.findTeacherById(teacherId);
        TeacherVO teacherVO = new TeacherVO();
        BeanUtils.copyProperties(teacher, teacherVO);

        Page<Course> page = new Page<>(1, 10);
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.eq("teacher_id", teacherId);
        List<Course> courses = courseService.findCoursesPage(page, qw).getRecords();
        teacherVO.setCourseNum(courseService.findCoursesPage(page,qw).getTotal());
        List<CourseList> courseLists = ListCopyUtil.copyCourseListToVo(courses);
        teacherVO.setCourseLists(courseLists);
        teacherVO.setStudyNum(folderService.teacherStudyNum(teacherVO.getCourseLists()));
        return teacherVO;
    }
}
