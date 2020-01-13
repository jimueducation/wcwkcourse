package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.mapper.FolderMapper;
import com.jimu.study.model.Course;
import com.jimu.study.model.vo.CourseList;
import com.jimu.study.service.CourseFolderService;
import com.jimu.study.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hxt
 */
@Service
public class CourseFolderServiceImpl implements CourseFolderService {

    private final int foot = 1;
    private final int favorites = 2;
    private final int buied = 3;
    private final int study = 4;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private CourseService courseService;

    @Override
    public Boolean insertFolder(Integer usersId, Integer courseId, Integer folderId) {
        switch (folderId){
            case foot:
                Integer footId = folderMapper.oneFoot(usersId, courseId);
                if(footId != null){
                    return folderMapper.updateFoot(new Date(), footId);
                }else {
                    return folderMapper.insertFoot(usersId, courseId, new Date());
                }
            case favorites:
                Integer favoritesId = folderMapper.oneFavorites(usersId, courseId);
                if(favoritesId != null){
                    return false;
                }else {
                    return folderMapper.insertFavorites(usersId, courseId);
                }
            case buied: return folderMapper.insertBuied(usersId, courseId);
            case study:
                Integer studyId = folderMapper.oneStudy(usersId, courseId);
                if(studyId != null){
                    return false;
                }else {
                    Course course = courseService.findOneCourse(courseId);
                    course.setStudyNum(course.getStudyNum() + 1);
                    courseService.updateCourse(course);
                    return folderMapper.insertStudy(usersId, courseId);
                }
            default: return false;
        }
    }

    @Override
    public List<CourseList> findCourseFolder(Integer usersId, Integer folderId) {
        Integer[] courseIdList;
        switch (folderId){
            case foot: courseIdList = folderMapper.footList(usersId); break;
            case favorites: courseIdList = folderMapper.favoritesList(usersId); break;
            case buied: courseIdList = folderMapper.buiedList(usersId); break;
            default: return null;
        }
        List<CourseList> courseLists = new ArrayList<>();
        for (Integer courseId: courseIdList){
            CourseList courseList = new CourseList();
            Course course = courseService.findOneCourse(courseId);
            BeanUtils.copyProperties(course, courseList);
            courseLists.add(courseList);
        }
        return courseLists;
    }

    @Override
    public Boolean deleteFavorites(Integer usersId, Integer courseId) {
        return folderMapper.deleteFavorites(courseId, usersId);
    }

    @Override
    public Integer isBuiedCourse(Integer usersId, Integer courseId) {
        return folderMapper.isBuiedCourse(usersId, courseId);
    }

    @Override
    public Integer teacherStudyNum(List<CourseList> courseLists) {
        List<Integer> course = new ArrayList<>();
        courseLists.forEach(e -> course.add(e.getCourseId()));
        QueryWrapper<Object> qw = new QueryWrapper<>();
        qw.in("course_id", course);
        return folderMapper.teacherStudyNum(qw).length;
    }
}
