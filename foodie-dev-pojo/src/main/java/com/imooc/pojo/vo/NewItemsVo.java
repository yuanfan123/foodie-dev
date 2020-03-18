package com.imooc.pojo.vo;

import java.util.List;

/**
 * @Classname CategoryVo
 * @Description 二级分类VO
 * @Date 2020/3/7 16:54
 * @Created by lyf
 */
public class NewItemsVo {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private  String catImage;
    private String bgColor;
    private List<SimpleItemVo> simpleItemList;

    public List<SimpleItemVo> getSimpleItemList() {
        return simpleItemList;
    }

    public void setSimpleItemList(List<SimpleItemVo> simpleItemList) {
        this.simpleItemList = simpleItemList;
    }

    public Integer getRootCatId() {
        return rootCatId;
    }

    public void setRootCatId(Integer rootCatId) {
        this.rootCatId = rootCatId;
    }

    public String getRootCatName() {
        return rootCatName;
    }

    public void setRootCatName(String rootCatName) {
        this.rootCatName = rootCatName;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
