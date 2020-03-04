package com.immmoc.enums;

/**
 * @Classname Sex
 * @Description 性别枚举
 * @Date 2020/3/2 23:36
 * @Created by lyf
 */
public enum Sex {
    wowen(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
