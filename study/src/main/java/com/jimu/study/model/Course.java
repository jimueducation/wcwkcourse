package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hxt
 */
@Data
public class Course {

    @TableId(value = "course_id", type = IdType.AUTO)
    private Integer courseId;

    @ApiModelProperty("课程分类ID")
    private Integer typeId;

    @ApiModelProperty("讲师ID")
    private Integer teacherId;

    @ApiModelProperty(value = "课程名称", required = true)
    private String courseName;

    @ApiModelProperty(value = "课程封面", required = true)
    private String courseIcon;

    @ApiModelProperty(value = "课程的价格", required = true)
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "是否完结", required = true, notes = "默认为未完结")
    private Boolean isOver;

    @ApiModelProperty("课程详细介绍")
    private String content;

    @ApiModelProperty(value = "课程目录的数量", required = true)
    private Integer courseNum;

    @ApiModelProperty(value = "学习人数", required = true, notes = "默认为0")
    private Integer studyNum;

    @ApiModelProperty("课程标签")
    private Integer courseLabel;

}
