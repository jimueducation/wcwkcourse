package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.TeacherMapper;
import com.jimu.study.model.Teacher;
import com.jimu.study.service.TeacherService;
import org.springframework.stereotype.Service;

/**
 * @author hxt
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public Integer insertTeacher(Teacher teacher) {
        return baseMapper.insert(teacher);
    }

    @Override
    public Integer updateTeacher(Teacher teacher) {
        return baseMapper.updateById(teacher);
    }

    @Override
    public Teacher findTeacherById(Integer teacherId) {
        return baseMapper.selectById(teacherId);
    }
}
