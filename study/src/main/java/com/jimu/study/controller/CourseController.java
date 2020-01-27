package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jimu.study.common.HttpResult;
import com.jimu.study.enums.FolderEnum;
import com.jimu.study.model.Course;
import com.jimu.study.model.CourseContent;
import com.jimu.study.model.vo.CourseContentVO;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.model.vo.CourseVO;
import com.jimu.study.quartz.ScheduledTask;
import com.jimu.study.service.CourseContentService;
import com.jimu.study.service.CourseFolderService;
import com.jimu.study.service.CourseService;
import com.jimu.study.service.TeacherService;
import com.jimu.study.utils.JwtUtil;
import com.jimu.study.utils.ListCopyUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    private static Integer firstSize = 8;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseContentService courseContentService;

    @Autowired
    private CourseFolderService folderService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("根据课程ID返回课程详情")
    @GetMapping("/findOneCourse")
    public HttpResult<CourseVO> findOneCourse(@RequestParam("courseId") Integer courseId,
                                              HttpServletRequest request) {
        String token = request.getHeader("cok");
        Course course = courseService.findOneCourse(courseId);
        if (course != null) {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(course, courseVO);
            //设置讲师信息
            courseVO.setTeacher(teacherService.findTeacherById(courseVO.getTeacherId()));
            //设置目录信息
            List<CourseContentVO> contentVos = new ArrayList<>();
            List<CourseContent> contents = courseContentService.findContentByCourseId(courseId);
            for (CourseContent content : contents) {
                CourseContentVO contentVO = new CourseContentVO();
                BeanUtils.copyProperties(content, contentVO);
                contentVos.add(contentVO);
            }
            courseVO.setCourseContent(contentVos);
            //如果已经登录则添加足迹列表
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                folderService.insertFolder((Integer) redisUtil.get(JwtUtil.getUsername(token)), courseId, FolderEnum.FOOT.getType());
            }
            return HttpResult.ok(courseVO);
        } else {
            return HttpResult.error("该课程已被删除");
        }
    }

    @ApiOperation(value = "根据分类ID返回课程列表", notes = "分类ID为0时为全部分类")
    @GetMapping("/findCourseList")
    public HttpResult<List<CourseList>> findCourseLise(@RequestParam("typeId") Integer typeId,
                                           @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent) {
        List<CourseList> courseList;
        Integer start = 0;
        if (pageCurrent == 1) {
            firstSize = pageSize;
            courseList = courseService.findCoursesList(typeId, start, pageSize);
        } else if (pageCurrent > 1) {
            start = (pageCurrent - 2) * pageSize + firstSize;
            courseList = courseService.findCoursesList(typeId, start, pageSize);
        } else {
            return HttpResult.error("页码不能小于1");
        }
        return HttpResult.ok(courseList);
    }

    @ApiOperation("搜索课程")
    @GetMapping("/searchCourse")
    public HttpResult<List<CourseList>> searchCourse(@RequestParam("search") String search,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent) {
        Page<CourseList> page = new Page<>(pageCurrent, pageSize);
        return HttpResult.ok(courseService.searchCourse(search, page));
    }

    @ApiOperation("返回最新课程")
    @GetMapping("/newestCourse")
    public HttpResult<List<CourseList>> newestCourse() {
        return HttpResult.ok(ScheduledTask.newestCourse);
    }

    @ApiOperation("返回热门课程")
    @GetMapping("/hotCourse")
    public HttpResult<List<CourseList>> hotCourse(@RequestParam("pageSize") Integer pageSize,
                                      @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent) {
        Page<CourseList> page = new Page<>(pageCurrent, pageSize);
        return HttpResult.ok(courseService.hotCourse(page));
    }

    @ApiOperation("根据价格升序排列课程")
    @GetMapping("/priceUp")
    public HttpResult<List<CourseList>> priceUp(@RequestParam("pageSize") Integer pageSize,
                                    @RequestParam("pageCurrent") Integer pageCurrent) {
        Page<CourseList> page = new Page<>(pageCurrent, pageSize);
        return HttpResult.ok(courseService.priceUpCourse(page));
    }
}
