package com.powernode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class) // 启用 SwaggerProperties 配置类
public class SwaggerAutoConfig {
    @Autowired
    private SwaggerProperties swaggerProperties;

    @Autowired
    private Environment environment;

    @Bean
    public Docket docket(){
        Boolean flag=true;
        String[] activeProfiles = environment.getActiveProfiles();
        for(String profile : activeProfiles) {
            if(activeProfiles.equals("pro")) {
                flag = false; // 如果是生产或测试环境，则不启用 Swagger
                break;
            }
        }
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .build(); // 根据环境变量决定是否启用 Swagger

    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                swaggerProperties.getTitle(), // 标题
                swaggerProperties.getDescription(), // 描述
                swaggerProperties.getVersion(), // 版本
                swaggerProperties.getTermsOfServiceUrl(), // 服务条款 URL
                new Contact(
                        swaggerProperties.getName(),
                        swaggerProperties.getUrl(),
                        swaggerProperties.getEmail()
                ), // 联系人信息
                swaggerProperties.getLicense(), // 许可证
                swaggerProperties.getLicenseUrl(), // 许可证 URL
                new HashSet<>() // 扩展信息
        );
    }
}
