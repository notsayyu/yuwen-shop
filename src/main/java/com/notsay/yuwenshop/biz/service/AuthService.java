package com.notsay.yuwenshop.biz.service;

import com.notsay.yuwenshop.web.param.LoginParam;
import com.notsay.yuwenshop.web.param.RegisterParam;
import com.notsay.yuwenshop.web.vo.LoginVO;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/19 19:51
 */
public interface AuthService {

    void register(RegisterParam param);

    LoginVO login(LoginParam param);
}