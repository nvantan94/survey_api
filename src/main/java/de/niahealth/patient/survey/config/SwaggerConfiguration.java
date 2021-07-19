package de.niahealth.patient.survey.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("survey-api")
                .apiInfo(
                        new ApiInfoBuilder().title("Survey API")
                        .description("Survey API reference for developers")
                        .version("1.0").build()
                )
                .directModelSubstitute(Byte.class, Integer.class)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.niahealth.patient.survey.controller"))
                .build();
    }
}
