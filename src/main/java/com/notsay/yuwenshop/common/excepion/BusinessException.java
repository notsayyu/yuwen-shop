package com.notsay.yuwenshop.common.excepion;

import com.notsay.yuwenshop.common.enums.Code;
import lombok.Getter;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 17:33
 */
@Getter
public class BusinessException extends RuntimeException {
    private Code code;

    private String msg;

    public BusinessException(Code code) {
        this(code, code.getMsg());
    }

    public BusinessException(Code code, String message) {
        super(message);
        this.code = code;
        msg = message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}