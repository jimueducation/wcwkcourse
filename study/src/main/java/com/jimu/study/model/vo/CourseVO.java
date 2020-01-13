package com.jimu.study.model.vo;

import com.jimu.study.model.Course;
import com.jimu.study.model.Teacher;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hxt
 */
@Data
public class CourseVO extends Course {

    private Boolean isOver;

    @ApiModelProperty("该课程的讲师")
    private Teacher teacher;

    @ApiModelProperty("课程的目录")
    private List<CourseContentVO> courseContent;

    public String getisOver(){
        if (isOver){
            return "已完结";
        }else{
            return "未完结";
        }
    }
}
