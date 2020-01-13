package com.jimu.study.service;

import com.jimu.study.model.vo.CourseList;

import java.util.List;

/**
 * @author hxt
 */
public interface CourseFolderService {

    /**根据标签插入对应的文件夹*/
    Boolean insertFolder(Integer usersId, Integer courseId, Integer folderId);

    /**根据标签返回对应文件夹的课程列表*/
    List<CourseList> findCourseFolder(Integer usersId, Integer folderId);

    /**删除收藏夹的一条内容*/
    Boolean deleteFavorites(Integer usersId, Integer courseId);

    /**判断是否购买该课程*/
    Integer isBuiedCourse(Integer usersId, Integer courseId);

    /**返回一个讲师的学习人数*/
    Integer teacherStudyNum(List<CourseList> courseLists);
}
