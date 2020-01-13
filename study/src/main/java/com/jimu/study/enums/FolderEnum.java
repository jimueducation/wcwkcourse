package com.jimu.study.enums;

import lombok.Getter;

/**
 * @author hxt
 */
@Getter
public enum FolderEnum {
    /**足迹*/
    FOOT(1),
    /**收藏夹*/
    FAVORITES(2),
    /**已购买的课程*/
    BUIED(3)
    ;

    private Integer type;

    FolderEnum(Integer type) {
        this.type = type;
    }
}
