package com.immmoc.enums;

/**
 * @Classname CommentLevel
 * @Description 商品评价等级的枚举
 * @Date 2020/3/2 23:36
 * @Created by lyf
 */
public enum CommentLevel {
    GOOD(1,"好评"),
    NORMAL(2,"中评"),
    BAD(3,"差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
