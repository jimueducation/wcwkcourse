package com.jimu.study.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hxt
 */
@Data
public class CourseContentVO {

    private Integer contentId;

    @ApiModelProperty("目录的名称")
    private String contentName;

    @ApiModelProperty("目录的类型")
    private Integer contentType;

    @ApiModelProperty("时长")
    private String contentTime;

    public String getContentType(){
        switch (contentType){
            case 1: return "图文";
            case 2: return "视频";
            case 3: return "音频";
            case 4: return "外连";
            default: return "";
        }
    }
}
