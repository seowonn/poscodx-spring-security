package config.web;

import config.WebConfig;
import config.app.SecurityConfigEx02;
import jakarta.servlet.Filter;

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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={WebConfig.class, SecurityConfigEx02.class})
@WebAppConfiguration
public class SecurityConfigEx02Test {
    private MockMvc mvc;
    private FilterChainProxy filterChainProxy;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        filterChainProxy = (FilterChainProxy)context.getBean("springSecurityFilterChain", Filter.class);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new DelegatingFilterProxy(filterChainProxy), "/*")
                .build();
    }
    
    // "/assets/**", "/**" 경로로 2개가 생성됨
    @Test
	public void testSecurityFilterChains() {
		List<SecurityFilterChain> securityFilterChains = filterChainProxy
				.getFilterChains();
		assertEquals(2, securityFilterChains.size());
	}
	
	@Test
	public void testSecurityFilterChain01() {
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains()
				.getFirst();
		assertEquals(0, securityFilterChain.getFilters().size());
	}

	// Spring Security 5.7 이상 또는 Spring Boot 3.x 이상에서는 필터 개수 10개
	@Test
	public void testSecurityFilterChain02() {
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains()
				.getLast();
		List<Filter> filters = securityFilterChain.getFilters();
		
		for(Filter filter : filters) {
			System.out.println(filter.getClass().getSimpleName());
		}
		
		assertEquals(10, filters.size());
	}

	@Test
	public void testAssets() throws Throwable {
		mvc.perform(get("/assets/images/logo.svg")).andExpect(status().isOk())
				.andExpect(content().contentType("image/svg+xml")).andDo(print());
	}

	@Test
	public void testHello() throws Throwable {
		mvc.perform(get("/ping")).andExpect(status().isOk())
				.andExpect(content().string("pong")).andDo(print());
	}
}
