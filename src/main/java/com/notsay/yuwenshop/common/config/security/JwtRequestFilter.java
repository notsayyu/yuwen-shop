package com.notsay.yuwenshop.common.config.security;

import com.notsay.yuwenshop.common.constants.BaseConstants;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import com.notsay.yuwenshop.common.utils.JwtUtil;
import com.notsay.yuwenshop.domain.entity.main.UserEntity;
import com.notsay.yuwenshop.domain.repository.main.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 19:20
 */

/**
 * OncePerRequestFilter是Spring Boot里面的一个过滤器抽象类，其同样在Spring Security里面被广泛用到
 * 这个过滤器抽象类通常被用于继承实现并在每次请求时只执行一次过滤
 */
@Configuration
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    //https://blog.csdn.net/qq_39361915/article/details/113261274
    private final HandlerExceptionResolver resolver;

    private final UserRepo userRepo;

    public JwtRequestFilter(JwtUtil jwtUtil, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
            , UserRepo userRepo) {
        this.jwtUtil = jwtUtil;
        this.resolver = resolver;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            //解析BxmToken
            String headToken = httpServletRequest.getHeader(BaseConstants.TOKEN_HEAD_NAME);
            parseAndCheckToken(headToken);
        } catch (Exception e) {
            log.info("解析token时发生错误", e);
            resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        } finally {
            SecurityContextHolder.clearContext();
        }


    }

    /**
     * 解析bxm的token,设置SecurityContext
     *
     * @return
     */
    private void parseAndCheckToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            String userCode = jwtUtil.getUserCodeFormToken(token);
            UserEntity userEntity = userRepo.findById(Integer.valueOf(userCode))
                    .orElseThrow(() -> new BusinessException(Code.USER_NOT_EXIST));
            SecureUserInfo secureUserInfo = SecureUserInfo.genFromUserEntity(userEntity);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(secureUserInfo, null, secureUserInfo.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}