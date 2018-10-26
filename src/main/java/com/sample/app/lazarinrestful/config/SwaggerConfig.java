package com.sample.app.lazarinrestful.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * Swagger config class
 * 
 * @author Lazarin, Carlos
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.sample.app.lazarinrestful"))
				.paths(regex("/.*")).build().apiInfo(metaData());

	}
	
	private ApiInfo metaData() {
        @SuppressWarnings("rawtypes")
		ApiInfo apiInfo = new ApiInfo(
                "Java 8 Transaction API",
                "Sample Java 8 Transaction API",
                "1.0",
                "Terms of service",
                new Contact("Carlos Lazarin", "", "carlos.joia.mail@gmail.com"),
               "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",new ArrayList<VendorExtension>());
        return apiInfo;
    }

}
