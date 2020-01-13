package com.jimu.study.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.model.Users;
import com.jimu.study.model.VipType;
import com.jimu.study.service.UsersService;
import com.jimu.study.service.VipTypeService;
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

    /** 认证 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken mtoken = (UsernamePasswordToken) authenticationToken;
        String username = mtoken.getUsername();
        if(username == null){
            throw new AccountException("没接收到用户名");
        }
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_name", username);
        List<Users> user = usersService.findUserList(qw);
        if(user.isEmpty()){
            throw new UnknownAccountException("没找到用户");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, user.get(0).getUsersPassword(), getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(user.get(0).getUsersSalt()));
        return info;
    }

    /** 授权 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if(principalCollection == null){
            throw new AuthorizationException("未找到请求体");
        }
        String username = (String) getAvailablePrincipal(principalCollection);
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_name", username);
        List<Users> users = usersService.findUserList(qw);
        VipType vipType = vipTypeService.findVipById(users.get(0).getVipId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermission(vipType.getVipName());
        return info;
    }
}
