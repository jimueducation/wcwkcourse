package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.CourseTypeMapper;
import com.jimu.study.model.CourseType;
import com.jimu.study.service.CourseTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements CourseTypeService {

    @Override
    public Integer insertCourseType(CourseType courseType) {
        return baseMapper.insert(courseType);
    }

    @Override
    public Integer updateCourseType(CourseType courseType) {
        return baseMapper.updateById(courseType);
    }

    @Override
    public List<Map<String, Object>> findAllCourseType(QueryWrapper<CourseType> qw) {
        return baseMapper.selectMaps(qw);
    }
}
