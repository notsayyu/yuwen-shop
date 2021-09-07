package com.notsay.yuwenshop.web.controller;

import com.notsay.yuwenshop.biz.service.AuthService;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.response.BaseResponse;
import com.notsay.yuwenshop.web.param.LoginParam;
import com.notsay.yuwenshop.web.param.RegisterParam;
import com.notsay.yuwenshop.web.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 15:20
 */
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "登录相关")
@Slf4j
public class AuthController {
    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ApiOperation("注册")
    public BaseResponse register(@RequestBody RegisterParam param) {
        authService.register(param);
        return BaseResponse.with(Code.SUCCESS);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public BaseResponse<LoginVO> login(@RequestBody LoginParam param) {

        return BaseResponse.with(Code.SUCCESS, authService.login(param));
    }


}