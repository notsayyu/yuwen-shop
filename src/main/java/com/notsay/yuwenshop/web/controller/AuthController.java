package com.notsay.yuwenshop.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    @GetMapping("/access")
    @ApiOperation("测试1")
    public void access(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        log.info("access sessionId={}", session.getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        response.setStatus(HttpStatus.FOUND.value());
//        response.setHeader(HttpHeaders.LOCATION, "http://localhost:9091/api/v1/test/access2");
        response.sendRedirect("http://localhost:9091/api/v1/test/access2");
    }
}