package com.notsay.yuwenshop.biz.service.impl;

import com.notsay.yuwenshop.biz.service.AuthService;
import com.notsay.yuwenshop.common.config.security.SecureUserInfo;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import com.notsay.yuwenshop.common.utils.GenCodeUtils;
import com.notsay.yuwenshop.common.utils.JwtUtil;
import com.notsay.yuwenshop.common.utils.Sm3Utils;
import com.notsay.yuwenshop.domain.entity.main.UserEntity;
import com.notsay.yuwenshop.domain.repository.main.UserRepo;
import com.notsay.yuwenshop.web.param.LoginParam;
import com.notsay.yuwenshop.web.param.RegisterParam;
import com.notsay.yuwenshop.web.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/19 19:52
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    final UserRepo userRepo;
    final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepo userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterParam param) {
        //1、判断密码是否一致
        if (!StringUtils.equals(param.getPassword(), param.getRePassword())) {
            throw new BusinessException(Code.PARAM_ERROR, "两次输入密码不一致");
        }
        //2、校验是否存在用户名
        UserEntity userEntity = userRepo.findByUsername(param.getUsername());
        if (Objects.nonNull(userEntity)) {
            throw new BusinessException(Code.PARAM_ERROR, "用户名已存在");
        }

        userEntity = UserEntity.builder()
                .userCode("")
                .username(param.getUsername())
                .mobile(param.getMobile())
                .pwd(Sm3Utils.encrypt(param.getPassword()))
                .role(param.getRole())
                .build();
        userRepo.save(userEntity);
        userEntity.setUserCode(GenCodeUtils.genUserCode(userEntity.getId()));
        userRepo.save(userEntity);
    }

    @Override
    public LoginVO login(LoginParam param) {
        UserEntity userEntity = userRepo.findByUsername(param.getUsername());
        if (Objects.isNull(userEntity)) {
            throw new BusinessException(Code.PARAM_ERROR, "用户名不存在");
        }
        if (!Sm3Utils.verify(param.getPassword(), userEntity.getPwd())) {
            throw new BusinessException(Code.PARAM_ERROR, "密码错误");
        }
        LoginVO loginVO = new LoginVO();
        loginVO.setUsername(userEntity.getUsername());
        loginVO.setRole(userEntity.getRole());
        UserDetails userDetails = SecureUserInfo.genFromUserEntity(userEntity);
        String token = jwtUtil.generateToken(userDetails);
        loginVO.setToken(token);
        return loginVO;
    }
}