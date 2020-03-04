package com.jimu.study.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimu.study.common.HttpResult;
import com.jimu.study.common.HttpsClient;
import com.jimu.study.model.Users;
import com.jimu.study.model.vo.UsersInformation;
import com.jimu.study.service.UsersService;
import com.jimu.study.utils.JwtUtil;
import com.jimu.study.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author hxt
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static String UPLOAD_FOLDER = "E:\\QMDownload\\study\\video";

    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public HttpResult<Object> login(@RequestParam("username") String username,
                                    @RequestParam("password") String password) {
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_name", username);
        Users users = usersService.getOne(qw);
        if (users == null) {
            return HttpResult.ok("账号不存在");
        } else {
            String salt = users.getUsersSalt();
            password = new Md5Hash(password, salt, 1).toHex();
            if (users.getUsersPassword().equals(password)){
                redisUtil.set(username, users.getUsersId());
                Map<String, String> userVo = new HashMap<>(0);
                userVo.put("usersId", users.getUsersId().toString());
                userVo.put("usersNick", users.getUsersNick());
                userVo.put("usersIcon", users.getUsersIcon());
                userVo.put("cookie", JwtUtil.sign(username, new Md5Hash(password, salt, 1).toHex()));
                return HttpResult.ok(userVo);
            } else {
                return HttpResult.error("密码错误");
            }
        }
    }

    @ApiOperation(value = "微信用户快捷登录")
    @GetMapping("/wechatLogin")
    public HttpResult<Object> wechatLogin(@RequestParam("code") String code,
                                          @RequestParam("state") String state,
                                          HttpServletRequest request){
        try {
            String access_token_reponse = HttpsClient.httpsRequestReturnString("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx0565484d8fa3a4fc&secret=817e51da938aa06b9f247e1356f73a52&code=" + code + "&grant_type=authorization_code", HttpsClient.METHOD_POST, null);
            JSONObject json =JSONObject.parseObject(access_token_reponse);
            String refresh = HttpsClient.httpsRequestReturnString("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=wx0565484d8fa3a4fc&grant_type=refresh_token&refresh_token=" + json.getString("refresh_token"), HttpsClient.METHOD_POST, null);
            json = JSONObject.parseObject(refresh);
            String get_information = HttpsClient.httpsRequestReturnString("https://api.weixin.qq.com/sns/userinfo?access_token=" + json.getString("access_token") + "&openid=" + json.getString("openid") + "&lang=zh_CN", HttpsClient.METHOD_GET, null);
            json = JSONObject.parseObject(get_information);
            QueryWrapper<Users> qw = new QueryWrapper<>();
            qw.like("users_name", json.getString("openid"));
            List<Users> user = usersService.findUserList(qw);
            if (user.isEmpty()) {
                register(json.getString("openid"), json.getString("openid"));
            }
            Users users = usersService.getOne(qw);
            users.setUsersIcon(json.getString("headimgurl"));
            users.setUsersNick(json.getString("nickname"));
            update(users, request);
            return login(json.getString("openid"), json.getString("openid"));
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("登录失败");
        }
    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping("/register")
    /** @PostMapping("/") */
    public HttpResult<Object> register(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.like("users_name", username);
        List<Users> user = usersService.findUserList(qw);
        if (user.isEmpty()) {
            usersService.registerUser(username, password);
            return HttpResult.ok("注册成功");
        } else {
            return HttpResult.ok("账号已存在");
        }
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    @RequiresAuthentication
    public HttpResult<Object>  logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("cok");
            redisUtil.del(JwtUtil.getUsername(token));
            return HttpResult.ok("登出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error("登出失败");
        }
    }

    @ApiOperation("返回用户的基本资料")
    @GetMapping("/getUsersInformation")
    @RequiresAuthentication
    /** @GetMapping("/") */
    public HttpResult<UsersInformation> getUsersInformation(HttpServletRequest request) {
        String token = request.getHeader("cok");
        Integer usersId = (Integer) redisUtil.get(JwtUtil.getUsername(token));
        Users users = usersService.getById(usersId);
        UsersInformation usersInformation = new UsersInformation();
        BeanUtils.copyProperties(users, usersInformation);
        return HttpResult.ok(usersInformation);
    }

    @ApiOperation(value = "修改用户基本资料接口")
    @PostMapping("update")
    @RequiresAuthentication
    /** @PutMapping("/") */
    public HttpResult<Object> update(@RequestBody Users users,
                                     HttpServletRequest request) {
        String token = request.getHeader("cok");
        users.setUsersId((Integer) redisUtil.get(JwtUtil.getUsername(token)));
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("users_id", users.getUsersId());
        List<Users> user = usersService.findUserList(qw);
        if (user.isEmpty()) {
            return HttpResult.ok("账号不存在");
        } else {
            try {
                users.setUsersSalt(user.get(0).getUsersSalt());
                users.setUsersIcon(user.get(0).getUsersIcon());
                usersService.updateUsers(users);
                return HttpResult.ok("修改成功");
            } catch (Exception e) {
                return HttpResult.error("发生未知错误");
            }
        }
    }

    @ApiOperation("上传用户头像")
    @PostMapping("/uploadIcon")
    @RequiresAuthentication
    public HttpResult<Object> fileUpload(@RequestParam("file") MultipartFile srcFile,
                                         HttpServletRequest request) {
        String token = request.getHeader("cok");
        System.out.println(srcFile.getOriginalFilename());
        if (srcFile.isEmpty()) {
            return HttpResult.ok("请选择文件");
        }
        try {
            /*String fileName = srcFile.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();*/
            String newFileName = JwtUtil.getUsername(token) + ".jpg";
            File destFile = new File(UPLOAD_FOLDER);
            if (!destFile.exists()) {
                destFile = new File("");
            }
            /*File files = new File(destFile, fileName);
            srcFile.transferTo(files);*/
            byte[] bytes = srcFile.getBytes();
            Path path = Paths.get(destFile.getAbsolutePath() + "/UsersIcon/" + newFileName);
            Files.write(path, bytes);
            Users user = new Users();
            user.setUsersId((Integer) redisUtil.get(JwtUtil.getUsername(token)));
            user.setUsersIcon("UsersIcon/" + newFileName);
            usersService.updateUsers(user);
            Map<String, String> filePath = new HashMap<>(0);
            filePath.put("filePath", "UsersIcon/" + newFileName);
            return HttpResult.ok("上传成功", filePath);
        } catch (IOException e) {
            return HttpResult.error("上传失败！");
        }
    }

    @ApiOperation("删除用户的接口")
    @DeleteMapping("/delete")
    @RequiresAuthentication
    /** @DeleteMapping("/") */
    public HttpResult<Object> delete(@RequestParam("usersId") Integer usersId) {
        try {
            usersService.deleteUser(usersId);
            return HttpResult.ok("删除成功");
        } catch (Exception e) {
            return HttpResult.error("发生未知错误");
        }
    }

    @ApiOperation(value = "保留，shiro拦截后暂时访问这个接口")
    @RequestMapping("/error")
    public HttpResult<Object> error() {
        System.out.println("error");
        return HttpResult.ok("请先登录");
    }
}
