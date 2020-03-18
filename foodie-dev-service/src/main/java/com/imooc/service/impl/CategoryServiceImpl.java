package com.imooc.service.impl;

import com.imooc.mapper.CategoryMapper;
import com.imooc.mapper.CategoryMapperCustom;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVo;
import com.imooc.pojo.vo.NewItemsVo;
import com.imooc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname CarouselServiceImpl
 * @Description
 * @Date 2020/3/7 10:54
 * @Created by lyf
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("type",1);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryVo> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<NewItemsVo> getSixNewItemsLazy(Integer rootCatId) {
        Map<String,Object> map = new HashMap<>();
        map.put("rootCatId",rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}
