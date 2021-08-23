package com.notsay.yuwenshop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/23 17:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("登录返回VO")
public class LoginVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户角色")
    private Integer role;

    @ApiModelProperty("token")
    private String token;
}