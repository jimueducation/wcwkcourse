package com.jimu.study.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jimu.study.model.Course;
import com.jimu.study.model.vo.CourseList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author hxt
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据不同的条件查找课程列表
     * @param query 条件构造器
     * @param page 分页
     * @return 课程列表
     */
    @Select("select course_id, course_name, course_icon, study_num, course_price, course_num, is_over from course ${ew.customSqlSegment}")
    List<CourseList> searchCourse(@Param(Constants.WRAPPER) Wrapper query, IPage<CourseList> page);

    @Select({"<script>",
            "select course_id, course_name, course_icon, study_num, course_price, course_num, is_over from course",
            "<if test='typeId != 0'>where type_id = #{typeId}</if>",
            "limit #{start}, #{size}",
            "</script>"})
    List<CourseList> selectCourseList(Integer typeId, Integer start, Integer size);

    @Update("update course set course_label = 0")
    Integer emptyLabel();

    @Update("update course set course_label = 2 ${ew.customSqlSegment}")
    Integer updateNewest(@Param(Constants.WRAPPER) Wrapper query);

    @Update("update course set course_label = 3 order by study_num limit 100")
    Integer updateHotest();
}
