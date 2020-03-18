package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;

import java.util.List;

/**
 * @Classname CarouselService
 * @Description
 * @Date 2020/3/7 10:52
 * @Created by lyf
 */
public interface CategoryService {
    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类id 查询子分类信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVo> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下的6条最新商品数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVo> getSixNewItemsLazy(Integer rootCatId);
}
