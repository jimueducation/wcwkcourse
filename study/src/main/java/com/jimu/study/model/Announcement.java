package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hxt
 */
@Data
public class Announcement {

    @TableId(value = "anno_id", type = IdType.AUTO)
    private Integer annoId;

    @ApiModelProperty(value = "公告的图标", required = true)
    private String annoIcon;

    @ApiModelProperty(value = "公告的标题", required = true)
    private String annoTitle;

    @ApiModelProperty(value = "公告的内容", required = true)
    private String context;

    @ApiModelProperty("发布的人")
    private String editor;

    @ApiModelProperty("修改时间")
    private Date createTime;

    @ApiModelProperty("阅读数")
    private Integer readNum;
}
