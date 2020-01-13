package com.jimu.study.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jimu.study.enums.UserSexEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hxt
 */
@Data
public class Users {

    @TableId(value = "users_id", type = IdType.AUTO)
    private Integer usersId;

    @ApiModelProperty("VIP的ID")
    private Integer vipId;

    @ApiModelProperty(value = "用户名", required = true)
    private String usersName;

    @ApiModelProperty(value = "密码", required = true)
    private String usersPassword;

    @ApiModelProperty("用户头像链接地址")
    private String usersIcon;

    @ApiModelProperty("用户的昵称")
    private String usersNick;

    @ApiModelProperty("用户的真实姓名")
    private String usersRealname;

    @ApiModelProperty(value = "用户性别", notes = "0为保密，1为男，2为女，默认为0")
    private Integer usersSex = UserSexEnum.SECRET.getSex();

    @ApiModelProperty("用户出生日期")
    private Date usersBirth;

    @ApiModelProperty("用户地址")
    private String usersAddress;

    @ApiModelProperty("用户VIP剩余时间")
    private Date usersVip;

    @ApiModelProperty("用户加密盐")
    private String usersSalt;

    public String getUsersVip(){
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(usersVip);
        }catch (NullPointerException e){
            return null;
        }
    }
}
