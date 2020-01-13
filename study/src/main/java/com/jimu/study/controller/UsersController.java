package com.jimu.study.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.model.Users;
import com.jimu.study.service.UsersService;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Resource
    private RedisUtil redisUtil;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public Object  login(@RequestParam("username") String username,
                         @RequestParam("password") String password){
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_name", username);
        List<Users> users = usersService.findUserList(qw);
        if(users.isEmpty()){
            return "账号不存在";
        }else if(users.size() == 1){
            String salt = users.get(0).getUsersSalt();
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(new UsernamePasswordToken(username, new Md5Hash(password, salt, 1).toHex()));
                redisUtil.set(SecurityUtils.getSubject().getPrincipal().toString(), users.get(0).getUsersId());
                Map<String, String> userVo = new HashMap<>(0);
                userVo.put("usersId", users.get(0).getUsersId().toString());
                userVo.put("usersNick", users.get(0).getUsersNick());
                userVo.put("usersIcon", users.get(0).getUsersIcon());
                return userVo;
            }catch (Exception e){
                return "密码错误";
            }
        }
        return "数据库错误";
    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password){
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.like("users_name", username);
        List<Users> user = usersService.findUserList(qw);
        if(user.isEmpty()){
            usersService.registerUser(username, password);
            return "注册成功";
        }else{
            return "账号已存在";
        }
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    public String  logout(){
        try {
            Subject subject = SecurityUtils.getSubject();
            redisUtil.del(SecurityUtils.getSubject().getPrincipal().toString());
            subject.logout();
            return "登出成功";
        }catch (Exception e){
            e.printStackTrace();
            return "登出失败";
        }
    }

    @ApiOperation(value = "修改用户基本资料接口")
    @PostMapping("update")
    public String update(@RequestBody Users users){
        users.setUsersId((Integer) redisUtil.get(SecurityUtils.getSubject().getPrincipal().toString()));
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_id", users.getUsersId());
        if(usersService.findUserList(qw).isEmpty()){
            return "账号不存在";
        }else{
            try {
                usersService.updateUsers(users);
                return "修改成功";
            }catch (Exception e){
                return "发生未知错误";
            }
        }
    }

    @ApiOperation("删除用户的接口")
    @DeleteMapping("/delete")
    public String delete(@RequestParam("usersId") Integer usersId){
        try {
            usersService.deleteUser(usersId);
            return "删除成功";
        }catch (Exception e){
            return "发生未知错误";
        }
    }

    @ApiOperation(value = "保留，shiro拦截后暂时访问这个接口")
    @GetMapping("/error")
    public String error(){
        return "请先登录";
    }
}
