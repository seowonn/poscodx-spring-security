package config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfigEx05 {
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return new WebSecurityCustomizer() {
			@Override
			public void customize(WebSecurity web) {
				web.ignoring().requestMatchers(new AntPathRequestMatcher("/assets/**"));
			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin((formLogin) -> {
		formLogin.loginPage("/user/login");
		}).authorizeHttpRequests((authorizeRequests) -> {
		/* ACL */
		// ?()안에 중 하나
		authorizeRequests.requestMatchers(new RegexRequestMatcher(
			// 2번째 인자: HTTP method
			"^/board/?(write|delete|modify|reply).*$", null)).authenticated()
			.anyRequest().permitAll();
		});
		return http.build();
	}
}
