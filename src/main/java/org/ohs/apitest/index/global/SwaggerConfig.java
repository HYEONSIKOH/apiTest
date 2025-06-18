package org.ohs.apitest.index.global;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private Info info(){
        return new Info()
                .title("API Test")
                .version("1.0")
                .description("성능 및 기능 테스트를 위한 Server");
    }

    // JWT 토큰 사용을 위한 Swagger 자체 모듈
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("access-token", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")))
//                .addSecurityItem(new SecurityRequirement().addList("access-token"))
                .info(info());
    }
}
