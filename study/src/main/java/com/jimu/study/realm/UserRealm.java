package com.jimu.study.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.common.JwtToken;
import com.jimu.study.model.Users;
import com.jimu.study.model.VipType;
import com.jimu.study.service.UsersService;
import com.jimu.study.service.VipTypeService;
import com.jimu.study.utils.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author hxt
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UsersService usersService;

    @Autowired
    private VipTypeService vipTypeService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /** 认证 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AccountException("没接收到用户名");
        }
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_name", username);
        Users user = usersService.getOne(qw);
        if (user == null) {
            throw new UnknownAccountException("没找到用户");
        }
        if (!JwtUtil.verify(token, username, user.getUsersPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, "user_realm");
    }

    /** 授权 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthorizationException("未找到请求体");
        }
        String username = JwtUtil.getUsername(principalCollection.toString());
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_name", username);
        Users user = usersService.getOne(qw);
        VipType vipType = vipTypeService.findVipById(user.getVipId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermission(vipType.getVipName());
        return info;
    }
}
