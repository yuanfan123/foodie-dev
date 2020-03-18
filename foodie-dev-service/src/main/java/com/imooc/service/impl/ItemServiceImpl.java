package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.immmoc.enums.CommentLevel;
import com.immmoc.enums.YesOrNo;
import com.immmoc.utils.DesensitizationUtil;
import com.immmoc.utils.PagedGridResult;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVo;
import com.imooc.pojo.vo.ItemCommentVo;
import com.imooc.pojo.vo.SearchItemsVo;
import com.imooc.pojo.vo.ShopcartVo;
import com.imooc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.GeneratedValue;
import java.util.*;

/**
 * @Classname CarouselServiceImpl
 * @Description
 * @Date 2020/3/7 10:54
 * @Created by lyf
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemsById(String id) {
        return itemsMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelCountsVo queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts+normalCounts+badCounts;
        CommentLevelCountsVo commentLevelCountsVo = new CommentLevelCountsVo();
        commentLevelCountsVo.setTotalCounts(totalCounts);
        commentLevelCountsVo.setGoodCounts(goodCounts);
        commentLevelCountsVo.setNormalCounts(normalCounts);
        commentLevelCountsVo.setBadCounts(badCounts);
        return commentLevelCountsVo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedComments(String itemId, Integer level,Integer page,Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);
        //mybatis pageHelper
        /**
         * page:第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page,pageSize);
        List<ItemCommentVo> commentList = itemsMapperCustom.queryItemComments(map);
        commentList.forEach(n->{
            n.setNickname(DesensitizationUtil.commonDisplay(n.getNickname()));
        });
        PagedGridResult grid = setterPageGrid(page, commentList);
        return grid;
    }

    private PagedGridResult setterPageGrid(Integer page, List<?> list) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVo> list = itemsMapperCustom.searchItems(map);
        PagedGridResult grid = setterPageGrid(page, list);
        return grid;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVo> list = itemsMapperCustom.searchItemsByThirdCat(map);
        PagedGridResult grid = setterPageGrid(page, list);
        return grid;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopcartVo> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list,ids);
        return itemsMapperCustom.queryItemsBySpecIds(list);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemMainImageById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result==null?"":result.getUrl();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, int buyCount) {
        // synchronize ：不推荐使用，集群环境下无用，性能低下
        //锁数据库：不推荐，导致数据库性能低下
        // TODO 分布式锁 zookeeper 或redis
        //lockUtil.getLock(); -- 加锁
        //1.查询库存

        //2.判断库存、是否能够减少到0以下

        //lockUtil.unlock() --解锁
        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCount);
        if (result!=1){
            throw new RuntimeException("订单创建失败! 原因：库存不足");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }
}
