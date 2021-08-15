package com.notsay.yuwenshop.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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

    UNKNOWN(99, "未知");

    /**
     *
     */

    private int code;
    private String msg;

    UserRoleEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<Integer, UserRoleEnum> CODE_MAP = new HashMap<>();

    static {
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            CODE_MAP.put(userRoleEnum.getCode(), userRoleEnum);
        }
    }

    public static UserRoleEnum getRoleByCode(Integer code) {
        return CODE_MAP.getOrDefault(code, UNKNOWN);
    }
}