package com.whf.pan.swagger2;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import javax.annotation.Resource;

/**
 * @author whf
 * @className Swagger2Config
 * @description TODO
 * @date 2023/10/18 16:53
 */
@SpringBootConfiguration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Slf4j
public class Swagger2Config {

    @Resource
    private Swagger2ConfigProperties properties;



    @Bean
    public Docket panServerApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .enable(properties.isShow())
                .groupName(properties.getGroupName())
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
        log.info("Swagger2 已被成功加载!");
        log.info("The swagger2 have been loaded successfully!");
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                .contact(new Contact(properties.getContactName(), properties.getContactUrl(), properties.getContactName()))
                .version(properties.getVersion())
                .build();
    }

}
