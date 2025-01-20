package config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfigEx02 {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return new WebSecurityCustomizer() {
	        @Override
	        public void customize(WebSecurity web) {
	            web
	                .ignoring() // Spring Security 필터 체인을 통과하지 않고 무시할 요청을 정의
	                .requestMatchers(new AntPathRequestMatcher("/assets/**"));
	        }
	    };
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Throwable {
		// 기본적으로 Spring Security 에서 필요한 모든 기본 필터를 추가해줌.
		return http.build();
	}

}
