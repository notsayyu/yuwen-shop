package com.notsay.yuwenshop.common.enums;

import lombok.Getter;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/14 11:00
 */
@Getter
public enum UserRoleEnum {
    /**
     * 管理员
     */
    ADMIN(1, "管理员"),

    /**
     * 顾客
     */
    CUSTOMER(2, "顾客"),

    /**
     * 商家
     */
    BUSINESS(3, "商家"),

    ;

    /**
     *
     */

    private int code;
    private String msg;

    UserRoleEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}