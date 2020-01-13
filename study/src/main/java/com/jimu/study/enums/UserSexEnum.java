package com.jimu.study.enums;

import lombok.Getter;

/**
 * @author hxt
 */
@Getter
public enum UserSexEnum {
    SECRET(0),
    MAN(1),
    WOMAN(2)
    ;

    private Integer sex;

    UserSexEnum(Integer sex) {
        this.sex = sex;
    }
}
