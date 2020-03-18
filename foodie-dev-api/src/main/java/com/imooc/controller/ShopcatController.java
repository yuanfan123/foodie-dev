package com.imooc.controller;


import com.immmoc.utils.IMOOCJSONResult;
import com.imooc.pojo.bo.ShopcartBo;
import com.imooc.service.impl.TestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Api(value = "购物车接口controller",tags = {"购物车接口controller"})
@RestController
@RequestMapping("shopcart")
public class ShopcatController {
    private final static Logger logger = LoggerFactory.getLogger(ShopcatController.class);
    @PostMapping("/add")
    @ApiOperation(value = "添加商品到购物车", notes ="添加商品到购物车",httpMethod = "POST")
    public IMOOCJSONResult add(@ApiParam(name="userId",value = "用户id",required = true) @RequestParam String userId,
                               @RequestBody ShopcartBo shopcartBo,
                               HttpServletRequest request,
                               HttpServletResponse response){
        if (StringUtils.isBlank(userId)){
            return  IMOOCJSONResult.errorMsg("");
        }
        // TODO 前段用户登录的情况下，添加商品到购物车，会同事同步到redis缓存
        logger.info("购物车：{}",shopcartBo);
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/del")
    @ApiOperation(value = "从购物车中删除商品", notes ="从购物车中删除商品",httpMethod = "POST")
    public IMOOCJSONResult del(@ApiParam(name="userId",value = "用户id",required = true) @RequestParam String userId,
                               @RequestParam String itemSpecId){
        if (StringUtils.isBlank(userId)){
            return  IMOOCJSONResult.errorMsg("参数不能为空");
        }
        // TODO 前段用户登录的情况下，删除商品到购物车，会同事同步到redis缓存
        return IMOOCJSONResult.ok();
    }
}
