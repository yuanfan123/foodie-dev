package com.immmoc.enums;

/**
 * @Classname Sex
 * @Description 性别枚举
 * @Date 2020/3/2 23:36
 * @Created by lyf
 */
public enum YesOrNo {
    No(0,"否"),
    YES(1,"是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
