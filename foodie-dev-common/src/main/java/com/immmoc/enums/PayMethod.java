package com.immmoc.enums;

/**
 * @Classname PayMethod
 * @Description
 * @Date 2020/3/10 21:18
 * @Created by lyf
 */
public enum PayMethod {
    WEIXIN(1,"微信"),
    ALIPAY(2,"支付宝");
    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
