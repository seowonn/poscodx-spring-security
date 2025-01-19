package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"controller"})
// WebMvcConfigurer: Spring MVC의 설정을 사용자가 정의하기 위한 인터페이스
public class WebConfig implements WebMvcConfigurer {

	// 정적 리소스의 경로와 실제 파일 위치를 매핑하는 역할을 담당
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");
    }
}