package com.notsay.yuwenshop.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 15:02
 */
@Configuration
/**
 * EnableWebSecurity注解有两个作用,1: 加载了WebSecurityConfiguration配置类, 配置安全认证策略。
 * 2: 加载了AuthenticationConfiguration, 配置了认证信息。
 */
@EnableWebSecurity
/**
 * https://blog.csdn.net/chihaihai/article/details/104678864
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public static final String[] PERMIT_PATHS = new String[]{
            "/api/v1/auth/*",
            "/api/v1/test/*"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .antMatcher("/api/**")
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(PERMIT_PATHS).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable();

        //通过Spring Security设置Cache-Control  禁用缓存功能
        httpSecurity.headers().cacheControl();
        //保证security和应用都不创建session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 是否允许请求带有验证信息
        corsConfiguration.setAllowCredentials(true);
        // 允许访问的客户端域名
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        // 允许服务端访问的客户端请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许访问的方法名,GET POST等
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}