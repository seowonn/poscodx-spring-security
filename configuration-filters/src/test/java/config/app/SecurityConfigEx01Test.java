package config.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import config.WebConfig;
import jakarta.servlet.Filter;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class, SecurityConfigEx01.class })
@WebAppConfiguration
public class SecurityConfigEx01Test {
	private MockMvc mvc;
	private FilterChainProxy filterChainProxy;

	@BeforeEach
	public void setup(WebApplicationContext context) {
		filterChainProxy = (FilterChainProxy) context.getBean("springSecurityFilterChain",
			Filter.class);
		mvc = MockMvcBuilders
			.webAppContextSetup(context)
			.addFilter(new DelegatingFilterProxy(filterChainProxy), "/*")
			.build();
	}

	@Test
	public void testSecurityFilterChains() {
		/**
		 * assets/**와 /**(모든 경로) 경로를 무시하도록 정의된 2개의 체인을 의미 
		 */
		List<SecurityFilterChain> securityFilterChains = filterChainProxy
			.getFilterChains();
		assertEquals(2, securityFilterChains.size());
	}

	/**
	 * Spring Security 설정에서 필터의 개수가 달라지는 이유
	 * : HttpSecurity 설정에 따라 활성화되는 필터가 서로 다르기 때문!
	 * 기본에 비해 로그인 관련 필터들이 추가로 활성화됨 
	 */
	@Test
	public void testSecurityFilters() {
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains()
			.getLast();
		List<Filter> filters = securityFilterChain.getFilters();

		assertEquals(14, filters.size());

		assertEquals("UsernamePasswordAuthenticationFilter",
			filters.get(6).getClass().getSimpleName());

		assertEquals("DefaultResourcesFilter", filters.get(7).getClass().getSimpleName());

		assertEquals("DefaultLoginPageGeneratingFilter",
			filters.get(8).getClass().getSimpleName());

		assertEquals("DefaultLogoutPageGeneratingFilter",
			filters.get(9).getClass().getSimpleName());
	}

	@Test
	public void testWebSecurity() throws Throwable {
		mvc
			.perform(get("/assets/images/logo.svg"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("image/svg+xml"))
			.andDo(print());
	}

	@Test
	public void testHttpSecurity() throws Throwable {
		mvc
			.perform(get("/ping"))
			.andExpect(status().isOk())
			.andExpect(content().string("pong"))
			.andDo(print());
	}

}
