package com.jimu.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimu.study.mapper.UsersMapper;
import com.jimu.study.model.Users;
import com.jimu.study.service.UsersService;
import com.jimu.study.utils.PasswordUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author hxt
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Override
    public Integer registerUser(String username, String password) {
        String salt = PasswordUtil.getSalt();
        Users users = new Users();
        users.setUsersName(username);
        users.setUsersSalt(salt);
        users.setUsersPassword(PasswordUtil.encode(password, salt));
        users.setUsersVip(new Date());
        users.setUsersIcon("192.168.0.100:9001/defaultIcon.jpg");
        baseMapper.insert(users);
        return users.getUsersId();
    }

    @Override
    public Integer updateUsers(Users users) {
        if (users.getUsersPassword() != null) {
            users.setUsersPassword(new Md5Hash(users.getUsersPassword(), users.getUsersSalt(), 1).toHex());
        }
        return baseMapper.updateById(users);
    }

    @Override
    public List<Users> findUserList(QueryWrapper<Users> qw) {
        return baseMapper.selectList(qw);
    }

    @Override
    public Integer deleteUser(Integer usersId) {
        return baseMapper.deleteById(usersId);
    }

    @Override
    public Boolean isVip(Date date) {
        return null;
    }
}
