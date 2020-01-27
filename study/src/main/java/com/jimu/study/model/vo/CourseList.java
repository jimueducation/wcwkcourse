package com.jimu.study.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hxt
 */
@Data
public class CourseList {

    @ApiModelProperty("课程ID")
    private Integer courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程封面图片")
    private String courseIcon;

    @ApiModelProperty("学习人数")
    private Integer studyNum;

    @ApiModelProperty("课程价格")
    private BigDecimal coursePrice;

    @ApiModelProperty("课程目录数量")
    private Integer courseNum;

    @ApiModelProperty("是否完结")
    private Boolean  isOver;

    @ApiModelProperty("课程标签")
    private Integer courseLabel;

    public String  getIsOver() {
        try {
            if (isOver) {
                return "已完结";
            } else {
                return "未完结";
            }
        }catch (Exception e) {
            return "";
        }
    }

    public String getCourseLabel(){
        try {
            switch (courseLabel) {
                case 1: return "VIP";
                case 2: return "NEW";
                case 3: return "HOT";
                default: return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
