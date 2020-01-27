package com.jimu.study.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hxt
 * @date 2020/1/16 13:41
 */
@Data
public class UsersInformation {

    @ApiModelProperty("学号（用户ID）")
    private Integer usersId;

    @ApiModelProperty("用户头像地址链接")
    private String usersIcon;

    @ApiModelProperty("哟用户昵称")
    private String usersNick;

    @ApiModelProperty("用户真实姓名")
    private String usersRealname;

    @ApiModelProperty("用户性别")
    private Integer usersSex;

    @ApiModelProperty("用户生日")
    private Data usersBirth;

    @ApiModelProperty("用户地址")
    private String usersAddress;
}
