/**
 * BBD Service Inc
 * AllRight Reserved @2017
 */
package com.bbd.swagger;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 接口注释
 * 
 * @author xc
 * @version $Id: SwaggerConfig.java, v 0.1 2016年12月5日 下午1:12:17 xc Exp $
 */
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.bbd.controller")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("接口文档(所有编码为utf8)").version("1.0")/*                .description("&消息头(header)参数:token,signature(签名),timestamp(时间戳10位);</br>签名验证:参数-signature;值-Md5(timestamp=value&token=value&key=value)加密")
                                                                           */.build();
    }
}