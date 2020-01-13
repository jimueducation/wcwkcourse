package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hxt
 */
@Data
public class Teacher {

    @TableId(value = "teacher_id", type = IdType.AUTO)
    private Integer teacherId;

    @ApiModelProperty("讲师头像的链接地址")
    private String teacherIcon;

    @ApiModelProperty(value = "讲师的昵称", required = true)
    private String teacherName;

    @ApiModelProperty(value = "讲师介绍", required = true)
    private String teacherDetail;
}
