package com.imooc.pojo.vo;

import java.util.Date;

/**
 * @Classname ItemCommentVo
 * @Description 用于展示商品评价vo
 * @Date 2020/3/8 1:48
 * @Created by lyf
 */
public class ItemCommentVo {
    private Integer contentLevel;
    private String content;
    private String sepcName;
    private Date createdTime;
    private String userFace;
    private String nickname;

    public Integer getContentLevel() {
        return contentLevel;
    }

    public void setContentLevel(Integer contentLevel) {
        this.contentLevel = contentLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSepcName() {
        return sepcName;
    }

    public void setSepcName(String sepcName) {
        this.sepcName = sepcName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
