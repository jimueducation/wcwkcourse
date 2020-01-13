package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.jimu.study.utils.ListCopyUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/course")
public class CourseController {

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
    public CourseVO findOneCourse(@RequestParam("courseId") Integer courseId){
        Course course = courseService.findOneCourse(courseId);
        if(course != null) {
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
            try {
                folderService.insertFolder((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()), courseId, FolderEnum.FOOT.getType());
            } catch (NullPointerException e) {
                //TODO
            }
            return courseVO;
        }else{
            System.out.println("error");
            //TODO 返回错误信息
            return null;
        }
    }

    @ApiOperation(value = "根据分类ID返回课程列表", notes = "分类ID为0时为全部分类")
    @GetMapping("/findCourseList")
    public List<CourseList> findCourseLise(@RequestParam("typeId") Integer typeId,
                                           @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam("pageCurrent") Integer pageCurrent){
        Page<Course> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<Course> qw = new QueryWrapper<>();
        qw.eq(typeId != 0,"type_Id", typeId);
        List<Course> courses = courseService.findCoursesPage(page, qw).getRecords();
        return ListCopyUtil.copyCourseListToVo(courses);
    }

    @ApiOperation("搜索课程")
    @GetMapping("/searchCourse")
    public List<CourseList> searchCourse(@RequestParam("search") String search,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("pageCurrent") Integer pageCurrent){
        Page<CourseList> page = new Page<>(pageCurrent, pageSize);
        return courseService.searchCourse(search, page);
    }

    @ApiOperation("返回最新课程")
    @GetMapping("/newestCourse")
    public List<CourseList> newestCourse(){
        return ScheduledTask.newestCourse;
    }

    @ApiOperation("返回热门课程")
    @GetMapping("/hotCourse")
    public List<CourseList> hotCourse(@RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("pageCurrent") Integer pageCurrent){
        Page<CourseList> page = new Page<>(pageCurrent, pageSize);
        return courseService.hotCourse(page);
    }

    @ApiOperation("根据价格升序排列课程")
    @GetMapping("/priceUp")
    public List<CourseList> priceUp(@RequestParam("pageSize") Integer pageSize,
                                    @RequestParam("pageCurrent") Integer pageCurrent){
        Page<CourseList> page = new Page<>(pageCurrent, pageSize);
        return courseService.priceUpCourse(page);
    }
}
