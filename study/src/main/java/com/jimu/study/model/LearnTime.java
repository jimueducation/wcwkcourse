package com.jimu.study.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hxt
 */
@Data
public class LearnTime {

    @ApiModelProperty("总视频时长")
    private Integer videoTime;

    @ApiModelProperty("总图文时长")
    private Integer picTime;

    @ApiModelProperty("总音频时长")
    private Integer audioTime;

    @ApiModelProperty("每日视频时长")
    private Integer videoTimeDay;

    @ApiModelProperty("每日图文时长")
    private Integer picTimeDay;

    @ApiModelProperty("每日音频时长")
    private Integer audioTimeDay;
}
