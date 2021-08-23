package com.notsay.yuwenshop.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/19 19:26
 */
@ApiModel("注册参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam {

    @ApiModelProperty("用户名")
    @NotBlank
    private String username;

    @ApiModelProperty("密码")
    @NotBlank
    private String password;
}