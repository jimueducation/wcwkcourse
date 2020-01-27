package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.common.HttpResult;
import com.jimu.study.model.Course;
import com.jimu.study.model.CourseContent;
import com.jimu.study.model.Users;
import com.jimu.study.service.*;
import com.jimu.study.utils.JwtUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/courseContent")
public class CourseContentController {

    @Autowired
    private CourseContentService contentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CourseFolderService folderService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("根据ID返回课时详情")
    @GetMapping("/getContent")
    @RequiresAuthentication
    public HttpResult<Object> getContent(@RequestParam("contentId") Integer contentId,
                                         HttpServletRequest request) {
        String token = request.getHeader("cok");
        //判断是否免费
        CourseContent content = contentService.findContentById(contentId);
        Course course = courseService.findOneCourse(content.getCourseId());
        Integer usersId = (Integer) redisUtil.get(JwtUtil.getUsername(token));
        String free = "0.00";
        if (!free.equals(course.getCoursePrice().toString())) {
            //判断是否为VIP
            QueryWrapper<Users> qw = new QueryWrapper<>();
            qw.gt("users_vip", new Date()).eq("users_id", usersId);
            List<Users> users = usersService.findUserList(qw);
            if (users.isEmpty()) {
                //判断是否已购买该课程
                Integer isBuy = folderService.isBuiedCourse(usersId, course.getCourseId());
                if (isBuy == null) {
                    return HttpResult.ok("请先购买该课程");
                }
            }
        }
        folderService.insertFolder(usersId, course.getCourseId(), 4);
        return HttpResult.ok(content);
    }
}
