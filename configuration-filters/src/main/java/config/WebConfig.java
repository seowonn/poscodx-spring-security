package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig 역할: MVC 설정과 정적 resource handling을 정의한다.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "controller" })
public class WebConfig implements WebMvcConfigurer {

	// 정적 파일 요청을 처리하기 위한 리소스 핸들러 등록
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**") // 클라이언트가 요청하는 경로
				.addResourceLocations("classpath:/assets/"); // 정적 리소스 파일의 실제 위치
	}
}