package com.jimu.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.Teacher;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
public interface TeacherService extends IService<Teacher> {

    /**插入讲师信息*/
    Integer insertTeacher(Teacher teacher);

    /**修改讲师信息*/
    Integer updateTeacher(Teacher teacher);

    /**根据ID查找讲师*/
    Teacher findTeacherById(Integer teacherId);
}
