package com.notsay.yuwenshop.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @description: 测试
 * @author: dsy
 * @date: 2021/8/15 10:25
 */
@RestController
@RequestMapping("/api/v1/test")
@Slf4j
@Api(tags = "测试")
public class TestController {

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

    @GetMapping("/access2")
    @ApiOperation("测试2")
    public void access2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        log.info("access2 sessionId={}", session.getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        response.setStatus(HttpStatus.FOUND.value());
//        response.setHeader(HttpHeaders.LOCATION, "http://localhost:9090/api/v1/test/access3");
        response.sendRedirect("http://localhost:9091/api/v1/test/access3");
    }

    @GetMapping("/access3")
    @ApiOperation("测试3")
    public String access3(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("access3 sessionId={}", session.getId());
        return "access3 return success";
    }

    @GetMapping("/access4")
    @ApiOperation("测试4")
    public String access4(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("access4 sessionId={}", session.getId());
        return "access4 return success";
    }

    @GetMapping("/access5")
    @ApiOperation("测试5")
    public String access5(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("access5 sessionId={}", session.getId());
        return "access5 return success";
    }

    @GetMapping("/access6")
    @ApiOperation("测试6")
    public String access6(HttpServletRequest request, HttpServletResponse response, @RequestParam String errorMsg) {
        HttpSession session = request.getSession();
        log.info("access6 sessionId={}", session.getId());
        log.info(errorMsg);

        return errorMsg;
    }
}