package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import filter.RealFilter;
import jakarta.servlet.Filter;

@Configuration // Config 클래스들은 @Configuration을 붙여줘야 Spring 컨테이너에 등록된다!
public class AppConfig {

//	DelegatingFilterProxy로 등록할 필터를 호출하는 메서드. 해당 메서드명이 Bean으로 등록된다.
	@Bean
	public Filter realFilter() {
		return new RealFilter();
	}
	
}
