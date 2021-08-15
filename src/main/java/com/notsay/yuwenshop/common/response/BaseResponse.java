package com.notsay.yuwenshop.common.response;


import com.notsay.yuwenshop.common.enums.Code;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
//@ApiModel(value = "基本返回")
public class BaseResponse<T> {

    //    @ApiModelProperty(value = "状态码")
    private int code;

    //    @ApiModelProperty(value = "消息")
    private String msg;

    //    @ApiModelProperty(value = "数据")
    private T data;

    public BaseResponse(int code) {
        this(code, null, null);
    }

    public BaseResponse(int code, String msg) {
        this(code, msg, null);
    }

    private BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(Code code) {
        this(code, null);
    }

    public BaseResponse(Code code, T data) {
        this(code.getCode(), code.getMsg(), data);
    }

    public static BaseResponse with(Code code) {
        return with(code, null);
    }

    public static <T> BaseResponse<T> with(Code code, T data) {
        return new BaseResponse<>(code, data);
    }

    public static BaseResponse with(int code) {
        return with(code, null, null);
    }

    public static BaseResponse with(int code, String msg) {
        return with(code, msg, null);
    }

    public static <T> BaseResponse<T> with(int code, T data) {
        return with(code, null, data);
    }

    public static <T> BaseResponse<T> with(int code, String msg, T data) {
        return new BaseResponse<>(code, msg, data);
    }
}
