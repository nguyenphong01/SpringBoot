package com.vti.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Department Manager Application API", // title
                "This is API description for Application", // description
                "1.0.0", // version
                "Terms of service URL", // termsOfServiceUrl
                new Contact("VTI Academy", "https://www.facebook.com/VTIAcademy/", "info@vtiacademy.edu.vn"), // contact
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html", // license
                Collections.emptyList()); // vendors
    }
}

