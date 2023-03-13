package com.base;

import java.util.Arrays;
import com.base.config.BaseConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * BaseApplication.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.base"})
@Import({BaseConfiguration.class})
public class BaseApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }

    /**
     * ObjectMapper.
     *
     * @return ObjectMapper
     * @author vsangucho on 01/07/2021
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    /**
     * Open api bean definition.
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .security(Arrays.asList(new SecurityRequirement().addList("bearerAuth")))
            .components(new Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                    .bearerFormat("JWT")))
            .info(new io.swagger.v3.oas.models.info.Info().title("REST API Documentation")
                .description("REST API Documentation for services")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Azul Blue")
                    .email("lex.max2010@gmail.com")
                    .url("lex.max2010@gmail.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
