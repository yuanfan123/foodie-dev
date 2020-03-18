package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Classname UserBO
 * @Description
 * @Date 2020/3/2 23:22
 * @Created by lyf
 */
@ApiModel(value = "用户队形BO",description = "由用户传入的数据封装在此entity")
public class UserBO {
    @ApiModelProperty(value = "用户名", name = "username" ,example = "imooc",required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password" ,example = "imooc",required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword" ,example = "imooc",required = true)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
