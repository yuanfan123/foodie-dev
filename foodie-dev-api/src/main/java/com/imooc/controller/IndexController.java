package com.imooc.controller;


import com.immmoc.enums.YesOrNo;
import com.immmoc.utils.IMOOCJSONResult;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.impl.TestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("index")
@Api(value="首页",tags={"首页展示相关的接口"})
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService  categoryService;
    final static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图列表", notes ="获取首页轮播图列表",httpMethod = "GET")
    public IMOOCJSONResult carousel(){
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(carousels);
    }
    /**
     * 首页分类展示需求：
     * 1.第一次刷新主页查询大分类，渲染到展示首页
     * 2.如果鼠标上移到大分类，则加载其子分类内容，如果已经存在，则不需要加载
     */
    @GetMapping("/cats")
    @ApiOperation(value = "获取商品分类（一级分类）", notes ="获取商品分类（一级分类）",httpMethod = "GET")
    public IMOOCJSONResult cats(){
        List<Category> categories = categoryService.queryAllRootLevelCat();
        return IMOOCJSONResult.ok(categories);
    }

    @GetMapping("/subCat/{rootCatId}")
    @ApiOperation(value = "获取商品子分类", notes ="获取商品子分类",httpMethod = "GET")
    public IMOOCJSONResult subCat(@ApiParam(name="rootCatId",value = "一级分类id",required = true) @PathVariable Integer rootCatId){
        if (rootCatId==null){
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVo> list = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(list);
    }
    @GetMapping("/sixNewItems/{rootCatId}")
    @ApiOperation(value = "查询每一个一级分类下的最新6条商品数据", notes ="查询每一个一级分类下的最新6条商品数据",httpMethod = "GET")
    public IMOOCJSONResult sixNewItems(@ApiParam(name="rootCatId",value = "一级分类id",required = true) @PathVariable Integer rootCatId){
        if (rootCatId==null){
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVo> list = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }
}
