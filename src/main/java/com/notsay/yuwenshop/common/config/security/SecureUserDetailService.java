package com.notsay.yuwenshop.common.config.security;

import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import com.notsay.yuwenshop.domain.entity.main.UserEntity;
import com.notsay.yuwenshop.domain.repository.main.UserRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 17:23
 */
@Service
public class SecureUserDetailService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (StringUtils.isBlank(s)) {
            throw new BusinessException(Code.USERNAME_CAN_NOT_BE_EMPTY);
        }
        UserEntity userEntity = userRepo.findById(Integer.valueOf(s))
                .orElseThrow(() -> new BusinessException(Code.USER_NOT_EXIST));

        return SecureUserInfo.genFromUserEntity(userEntity);
    }
}