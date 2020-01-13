package com.jimu.study.enums;

import lombok.Getter;

/**
 * @author hxt
 */
@Getter
public enum CourseContentTypeEnum {
    /**图文类型*/
    PICTURE(1),
    /**视频类型*/
    VIDEO(2),
    /**音频类型*/
    AUDIO(3),
    /**外链类型*/
    LINK(4)
    ;

    private Integer type;

    CourseContentTypeEnum(Integer type) {
        this.type = type;
    }
}
