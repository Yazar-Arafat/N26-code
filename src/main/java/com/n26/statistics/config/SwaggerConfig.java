package com.n26.statistics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	public Docket newsApi() {

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.n26.statistics")).build();
	}
// Actuator endpoints - .paths(PathSelectors.any())
	
	private ApiInfo apiInfo() {

		return new ApiInfoBuilder().title(this.applicationName).description(this.applicationName).contact(contact())
				.version("0.0.1").build();
	}

	private Contact contact() {
		return new Contact("Yazar Arafath", "https://www.linkedin.com/in/yazar-arafath-p/", "yazararafath.p@gmail.com");
	}

}