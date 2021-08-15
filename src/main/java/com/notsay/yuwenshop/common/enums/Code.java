package com.notsay.yuwenshop.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 17:33
 */
@Getter
public enum Code {
    /**
     * 未定义
     */
    UNDEFINED(-1, "未定义"),

    /**
     * 业务状态码
     */
    SUCCESS(0, "success"),

    SYSTEM_ERROR(9999, "系统繁忙，请稍后再试"),

    UN_AUTHORIZATION(401, "当前用户未认证或认证过期,请重新登录"),

    PERMISSION_DENIED(402, "权限拒绝"),

    PARAM_ERROR(90, "参数异常"),

    /**
     * 用户相关 100-200
     */
    USERNAME_CAN_NOT_BE_EMPTY(100, "用户名不能为空"),

    USER_NOT_EXIST(101, "用户不存在"),

    /**
     * HTTP相关
     */
    HTTP_METHOD_NOT_SUPPORTED(1002, "请求方法错误"),

    HTTP_CONTENT_TYPE_NOT_SUPPORTED(1003, "不支持的Content-Type"),

    ;

    private int code;
    private String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private static final Map<Integer, Code> CODE_MAP = new HashMap<>();

    static {
        for (Code code : Code.values()) {
            CODE_MAP.put(code.code, code);
        }
    }

    public static Code getEnumByCode(int code) {
        return CODE_MAP.get(code);
    }

    public static boolean isValid(int code) {
        return CODE_MAP.get(code) != null;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }


}