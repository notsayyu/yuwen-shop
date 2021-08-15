package com.notsay.yuwenshop.common.utils;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 18:04
 */

import com.notsay.yuwenshop.common.config.security.SecureUserInfo;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * https://www.cnblogs.com/cndarren/p/11518443.html
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret:YUWEN_SHOP}")
    private String secret = "YUWEN_SHOP";

    @Value("${jwt.expire:1800}")
    private Long expire = 1800L;

    /**
     * 生成token
     */
    public String generateToken(UserDetails userInfo) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userInfo.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String userCode) {
        return Jwts.builder().setClaims(claims).setSubject(userCode).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }


    /**
     * 校验token && 判断时间是否过期
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserCodeFormToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 解析token获取唯一标识
     */
    public String getUserCodeFormToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(Code.UN_AUTHORIZATION, "token过期");
        }

    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
    }

    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        UserDetails userDetails = SecureUserInfo.builder()
                .userId(11)
                .username("小明")
                .role(1)
                .mobile("1111111111")
                .build();

        String token = jwtUtil.generateToken(userDetails);
        System.out.println(token);
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        System.out.println(claims.getSubject());
    }

}