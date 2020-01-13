package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hxt
 */
@Data
public class CourseContent {

    @TableId(value = "content_id", type = IdType.AUTO)
    private Integer contentId;

    @ApiModelProperty(value = "属于哪个课程的ID", required = true)
    private Integer courseId;

    @ApiModelProperty(value = "目录的名称", required = true)
    private String contentName;

    @ApiModelProperty(value = "目录的类型", notes = "1为图文，2为视频，3为音频，4为外链", required = true)
    private Integer contentType;

    @ApiModelProperty(value = "视频时长", notes = "如果不为视频，则为0")
    private String contentTime;

    @ApiModelProperty(value = "内容详情",required = true)
    private String content;
}
