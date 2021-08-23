package com.notsay.yuwenshop.web.param;

import com.notsay.yuwenshop.common.constants.RegexPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/19 19:26
 */
@ApiModel("注册参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterParam {

    @ApiModelProperty("用户名")
    @NotBlank
    private String username;

    @ApiModelProperty("密码")
    @NotBlank
    @Pattern(regexp = RegexPattern.PASSWORD_REGEX2, message = "请输入长度为8到16的非纯数字或纯字母密码")
    private String password;

    @ApiModelProperty("重复密码")
    @NotBlank
    @Pattern(regexp = RegexPattern.PASSWORD_REGEX2, message = "请输入长度为8到16的非纯数字或纯字母密码")
    private String rePassword;

    @ApiModelProperty("手机号")
    @NotBlank
    @Pattern(regexp = RegexPattern.MOBILE_ALL, message = "请输入正确的手机号")
    private String mobile;

    @ApiModelProperty("角色")
    @NotNull
    @Min(1)
    @Max(3)
    private Integer role;
}