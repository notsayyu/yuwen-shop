package com.notsay.yuwenshop.common.excepion;

import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 21:19
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("unchecked")
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException:" + e.getMessage());
        FieldError fe = (FieldError) e.getBindingResult().getAllErrors().get(0);
        String message = String.format("%s %s", fe.getField(), StringUtils.isNotBlank(fe.getDefaultMessage()) ? fe.getDefaultMessage() : e.getMessage());
        return BaseResponse.with(Code.PARAM_ERROR.getCode(), message);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException:" + e.getMessage());
        return BaseResponse.with(Code.PARAM_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e) {
        log.info("BusinessException:", e);
        return BaseResponse.with(e.getCode().getCode(), e.getMsg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException is :{}, ip is : {}", e.getMessage(), request.getRequestURI());
        return BaseResponse.with(Code.HTTP_METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BaseResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.warn(e.getMessage());
        return BaseResponse.with(Code.HTTP_CONTENT_TYPE_NOT_SUPPORTED);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public BaseResponse handleInsufficientAuthenticationException(InsufficientAuthenticationException e,
                                                                  HttpServletResponse response) {
        log.warn("来自未登录的请求:{}", e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return BaseResponse.with(Code.UN_AUTHORIZATION);
    }

    @ExceptionHandler
    public BaseResponse handleAuthenticationException(AuthenticationException e, HttpServletResponse response) {
        log.error("权限验证不通过,msg:[{}]", e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return BaseResponse.with(Code.UN_AUTHORIZATION);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BaseResponse handleJwtException(AccessDeniedException e) {
        log.error("AccessDeniedException:" + e.getMessage());
        return BaseResponse.with(Code.UN_AUTHORIZATION, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity(BaseResponse.with(Code.SYSTEM_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}