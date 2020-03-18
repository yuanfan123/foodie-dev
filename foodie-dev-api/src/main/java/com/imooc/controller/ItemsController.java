package com.imooc.controller;


import com.immmoc.enums.YesOrNo;
import com.immmoc.utils.IMOOCJSONResult;
import com.immmoc.utils.PagedGridResult;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.*;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.generic.RET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
@Api(value="商品接口",tags={"商品展示相关的接口"})
public class ItemsController extends BaseController{
    @Autowired
    private ItemService itemService;
    @GetMapping("/info/{itemId}")
    @ApiOperation(value = "查询商品详情", notes ="查询商品详情",httpMethod = "GET")
    public IMOOCJSONResult info(@ApiParam(name="itemId",value = "商品id",required = true)@PathVariable String itemId){
        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        Items item = itemService.queryItemsById(itemId);
        List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVo itemInfoVo = new ItemInfoVo();
        itemInfoVo.setItem(item);
        itemInfoVo.setItemImgList(itemsImgList);
        itemInfoVo.setItemSpecList(itemsSpecList);
        itemInfoVo.setItemParams(itemsParam);
        return IMOOCJSONResult.ok(itemInfoVo);
    }
    @GetMapping("/commentLevel")
    @ApiOperation(value = "查询商品评价等级", notes ="查询商品评价等级",httpMethod = "GET")
    public IMOOCJSONResult commentLevel(@ApiParam(name="itemId",value = "商品id",required = true)@RequestParam String itemId){
        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        CommentLevelCountsVo countsVo = itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(countsVo);
    }
    @GetMapping("/comments")
    @ApiOperation(value = "查询商品评价", notes ="查询商品评价",httpMethod = "GET")
    public IMOOCJSONResult comments(@ApiParam(name="itemId",value = "商品id",required = true)@RequestParam String itemId,
                                        @ApiParam(name="level",value = "评价等级",required = false)@RequestParam Integer level,
                                        @ApiParam(name="page",value = "查询下一页的第几页",required = false)@RequestParam Integer page,
                                        @ApiParam(name="pageSize",value = "每一页显示的条数",required = false)@RequestParam Integer pageSize){
        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page==null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(grid);
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索商品列表", notes ="搜索商品列表",httpMethod = "GET")
    public IMOOCJSONResult search(@ApiParam(name="keywords",value = "关键字",required = true)@RequestParam String keywords,
                                    @ApiParam(name="sort",value = "排序",required = false)@RequestParam String sort,
                                    @ApiParam(name="page",value = "查询下一页的第几页",required = false)@RequestParam Integer page,
                                    @ApiParam(name="pageSize",value = "每一页显示的条数",required = false)@RequestParam Integer pageSize){
        if (StringUtils.isBlank(keywords)){
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page==null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(grid);
    }
    @GetMapping("/catItems")
    @ApiOperation(value = "根据三级id搜索商品列表", notes ="根据三级id搜索商品列表",httpMethod = "GET")
    public IMOOCJSONResult catItems(@ApiParam(name="catId",value = "关键字",required = true)@RequestParam Integer catId,
                                  @ApiParam(name="sort",value = "排序",required = false)@RequestParam String sort,
                                  @ApiParam(name="page",value = "查询下一页的第几页",required = false)@RequestParam Integer page,
                                  @ApiParam(name="pageSize",value = "每一页显示的条数",required = false)@RequestParam Integer pageSize){
        if (catId == null){
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page==null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(grid);
    }

    /**
     * 用于用户长时间未登陆网址，刷新购物车数据
     * @param itemSpecIds
     * @return
     */
    @GetMapping("/refresh")
    @ApiOperation(value = "根据商品ids查找罪行的商品数据", notes ="根据商品ids查找罪行的商品数据",httpMethod = "GET")
    public IMOOCJSONResult refresh(@ApiParam(name="itemSpecIds",value = "拼接的规格ids",required = true,example = "1001,1002,1005")@RequestParam String itemSpecIds){
        if (StringUtils.isBlank(itemSpecIds)){
            return IMOOCJSONResult.ok();
        }
        List<ShopcartVo> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return IMOOCJSONResult.ok(list);
    }
}
