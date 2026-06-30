package study.springbootdeveloper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer  {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /upload/** 경로 요청이 오면 uploads/ 디렉터리의 파일을 제공
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
