package com.notsay.yuwenshop.common.config.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: ExceptionTranslationFilter中会捕获spring security的权限相关的异常, 捕获后调用AuthenticationEntryPoint进行处理. 代码中使用全局异常处理Bean进行处理
 * @author: dsy
 * @date: 2020/3/9 18:32
 */
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public AuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
    }
}
