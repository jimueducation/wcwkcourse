package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hxt
 */
@Data
public class CourseType {

    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    @ApiModelProperty(value = "课程分类名字", required = true)
    private String typeName;

    @ApiModelProperty("课程分类的图标的链接地址")
    private String typeIcon;
}
