package com.imooc.service;

import com.immmoc.utils.PagedGridResult;
import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVo;
import com.imooc.pojo.vo.ItemCommentVo;
import com.imooc.pojo.vo.ShopcartVo;

import java.util.List;

/**
 * @Classname CarouselService
 * @Description
 * @Date 2020/3/7 10:52
 * @Created by lyf
 */
public interface ItemService {
    /**
     * 根据商品id查询详情
     * @param id
     * @return
     */
    public Items queryItemsById(String id );

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品属性
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     * @param itemId
     */
    public CommentLevelCountsVo queryCommentCounts(String itemId);

    /**
     * 根据商品id 查评价（分页）
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据分类id搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询的最新的购物车中的商品数据（用于渲染购物车数据）
     * @param specIds
     * @return
     */
    public List<ShopcartVo> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品id查询规格对象具体信息
     * @param specId
     * @return
     */
    public ItemsSpec queryItemSpecById(String  specId);

    /**
     * 根据商品id获得商品主图
     * @param itemId
     * @return
     */
    public String queryItemMainImageById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCount
     */
    public void decreaseItemSpecStock(String specId , int buyCount);

}
