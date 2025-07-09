package com.powernode.config;

import io.swagger.annotations.SwaggerDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "swagger")
@NoArgsConstructor
public class SwaggerProperties {
    private String basePackage;

    private String name;

    private String email;

    private String url;

    private String title;
    /**
     * 描述
     */
    private String description;
    private String license;
    private String licenseUrl;
    private String termsOfServiceUrl;

    private String version;


}
