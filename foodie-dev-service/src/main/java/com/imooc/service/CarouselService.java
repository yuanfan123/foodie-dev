package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

/**
 * @Classname CarouselService
 * @Description
 * @Date 2020/3/7 10:52
 * @Created by lyf
 */
public interface CarouselService {
    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);
}
