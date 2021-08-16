package com.notsay.yuwenshop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 22:08
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        //版本类型是swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("宇文shop")
                //通过调用自定义方法apiInfo，获得文档的主要信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.notsay.yuwenshop"))//扫描该包下面的API注解
                .paths(PathSelectors.any())
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .securitySchemes(security())
                .securityContexts(securityContexts());
    }

    private List<SecurityScheme> security() {
        return newArrayList(
                new ApiKey("Authorization", "Authorization", "header")
        );
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        // 所有包含"auth"的接口不需要使用securitySchemes
//                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("Authorization", authorizationScopes));
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://127.0.0.1:8088/swagger-ui.html
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("宇文shop") //接口管理文档首页显示
                .description("宇文shop")//API的描
                .version("1.0")
                .build();
    }
}