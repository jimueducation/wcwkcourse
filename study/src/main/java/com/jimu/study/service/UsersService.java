package com.jimu.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jimu.study.model.Users;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
public interface UsersService extends IService<Users> {

    /**注册账号*/
    Integer registerUser(String username, String password);

    /**修改个人信息*/
    Integer updateUsers(Users users);

    /**查找账户信息*/
    List<Users> findUserList(QueryWrapper<Users> qw);

    /**删除用户.PS:不知道用不用的上*/
    Integer deleteUser(Integer usersId);

    /**判断VIP是否过期*/
    Boolean isVip(Date date);
}
