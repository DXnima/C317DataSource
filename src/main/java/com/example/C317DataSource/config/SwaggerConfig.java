package com.example.C317DataSource.config;

import com.example.C317DataSource.C317DataSourceApplication;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 *Swagger 配置类
 * @author wnm
 * @date 2020/9/25
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean(value = "1.0")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //这个controller包路径记得要修改为自己controller路径
                .apis(RequestHandlerSelectors.basePackage("com.example.C317DataSource"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("连接多数据源接口文档")
                .description("api接口文档")
                //BaseUrl,记得改为自己的（IP+port+context-path）
                .contact("DXnima")
                .termsOfServiceUrl("http://localhost:"+ C317DataSourceApplication.port+"/api/jdbc/")
                .version("1.0")
                .build();
    }
}
