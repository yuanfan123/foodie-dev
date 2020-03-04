package com.imooc.controller;


import com.immmoc.utils.CookieUtils;
import com.immmoc.utils.IMOOCJSONResult;
import com.immmoc.utils.JsonUtils;
import com.immmoc.utils.MD5Utils;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value="注册登录",tags={"用于注册登录相关的接口"})
@RestController
@RequestMapping("passport")
public class PassPortController {
    @Autowired
    private UserService userService;
    @ApiOperation(value = "用户名是否存在", notes ="用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        //1.判断入参不能为空
        if (StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        //2.查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //3.请求成功
        return IMOOCJSONResult.ok();

    }
    @PostMapping("/regist")
    @ApiOperation(value = "用户注册", notes ="用户注册",httpMethod = "POST")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        String username = userBO.getUsername();
        String pwd = userBO.getPassword();
        String comfirmPwd = userBO.getConfirmPassword();
        //0.判断用户名和密码必须不为空
        if (StringUtils.isBlank(username)||StringUtils.isBlank(pwd)||StringUtils.isBlank(comfirmPwd)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //1.判断用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //2. 密码长度不小于6位
        if (pwd.length()<6){
            return IMOOCJSONResult.errorMsg("密码长度不小于6位");
        }
        //3. 判断两次密码是否一致
        if (!pwd.equals(comfirmPwd)){
            return IMOOCJSONResult.errorMsg("两次密码是否一致");
        }
        //4.实现注册
        Users userResult = userService.createUser(userBO);
        setNullProperty(userResult);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes ="用户登录",httpMethod = "POST")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String pwd = userBO.getPassword();
        //0.判断用户名和密码必须不为空
        if (StringUtils.isBlank(username)||StringUtils.isBlank(pwd)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //2.实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(pwd));
        if (userResult==null){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        setNullProperty(userResult);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return IMOOCJSONResult.ok(userResult);
    }

    private void setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出", notes ="用户退出",httpMethod = "POST")
    public IMOOCJSONResult logout(@RequestParam String  userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        //清除用户相关的cookie
        CookieUtils.deleteCookie(request,response,"user");
        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式回话中需要清除用户数据

        return IMOOCJSONResult.ok();
    }

}
