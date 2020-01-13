package com.jimu.study.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * @author hxt
 */
@Mapper
public interface FolderMapper {

    /**
     * 返回用户的足迹列表，并根据更新时间进行降序
     * @param usersId 用户ID
     * @return 课程ID的数组
     */
    @Select("select course_id from foot where users_id = #{usersId} order by update_time DESC")
    Integer[] footList(Integer usersId);

    /**
     * 查找足迹列表里是否有该记录
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return 足迹ID
     */
    @Select("select foot_id from foot where users_id = #{usersId} and course_id = #{courseId}")
    Integer oneFoot(Integer usersId, Integer courseId);

    /**
     * 返回用户收藏夹列表
     * @param usersId 用户ID
     * @return 课程ID的数组
     */
    @Select("select course_id from favorites where users_id = #{usersId}")
    Integer[] favoritesList(Integer usersId);

    /**
     * 查看课程是否已净收藏
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return 收藏夹ID
     */
    @Select("select favorites_id from favorites where users_id = #{usersId} and course_id = #{courseId}")
    Integer oneFavorites(Integer usersId, Integer courseId);

    /**
     * 返回用户已购买的课程ID数组
     * @param usersId 用户ID
     * @return 课程ID数组
     */
    @Select("select course_id from buied where users_id = #{usersId}")
    Integer[] buiedList(Integer usersId);

    /**
     * 返回讲师的学习人数
     * @param query 条件构造器
     * @return 去重用户ID的数组
     */
    @Select("select DISTINCT users_id from course_study ${ew.customSqlSegment}")
    Integer[] teacherStudyNum(@Param(Constants.WRAPPER) Wrapper query);

    /**
     * 判断是否已经有数据了
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return 学习人数ID
     */
    @Select("select study_id from course_study where users_id = #{usersId} and course_id = #{courseId}")
    Integer oneStudy(Integer usersId, Integer courseId);

    /**
     * 判断用户是否已经购买该课程
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return 课程ID
     */
    @Select("select course_id from buied where users_id = #{usersId} and course_id = #{courseId}")
    Integer isBuiedCourse(Integer usersId, Integer courseId);

    /**
     * 插入数据到足迹
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @param date 创建时间
     * @return boolean
     */
    @Insert("insert into foot(users_id, course_id, update_time) values(#{usersId}, #{courseId}, #{date})")
    Boolean insertFoot(Integer usersId, Integer courseId, Date date);

    /**
     * 插入数据到收藏夹
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return boolean
     */
    @Insert("insert into favorites(users_id, course_id) values(#{usersId}, #{courseId})")
    Boolean insertFavorites(Integer usersId, Integer courseId);

    /**
     * 插入数据到是否购买课程表
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return boolean
     */
    @Insert("insert into buied(users_id, course_id) values(#{usersId}, #{courseId})")
    Boolean insertBuied(Integer usersId, Integer courseId);

    /**
     * 插入数据到学习表
     * @param usersId 用户ID
     * @param courseId 课程ID
     * @return boolean
     */
    @Insert("insert into course_study(users_id, course_id) values(#{usersId}, #{courseId})")
    Boolean insertStudy(Integer usersId, Integer courseId);

    /**
     * 删除收藏夹里的课程
     * @param courseId 课程ID
     * @param usersId 用户ID
     * @return boolean
     */
    @Delete("delete from favorites where course_id = #{courseId} and users_id = #{usersId}")
    Boolean deleteFavorites(Integer courseId, Integer usersId);

    /**
     * 修改足迹的访问时间
     * @param date 时间
     * @param footId 足迹ID
     * @return boolean
     */
    @Update("update foot set update_time = #{date} where foot_id = #{footId}")
    Boolean updateFoot(Date date, Integer footId);
}
