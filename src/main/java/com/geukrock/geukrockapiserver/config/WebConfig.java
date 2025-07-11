package com.geukrock.geukrockapiserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Value("${file.upload-path}")
    private String imageUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 요청을 로컬 디스크 경로에 매핑
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imageUrl + "/");
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em){
        return new JPAQueryFactory(em);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:[*]")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("Authorization", "Content-Type")
                        .exposedHeaders("Custom-Header")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
