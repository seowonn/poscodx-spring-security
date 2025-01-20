package config.web;

import jakarta.servlet.Filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

/**
 * @ContextConfiguration
 * Spring 애플리케이션 컨텍스트를 설정하기 위해 사용.
 * 테스트를 실행하기 전에 필요한 빈(Bean)과 설정을 로드하여 Spring 기반의 통합 테스트를 가능하게 함.
 * 아래는 classes 대신 locations 를 설정하여 xml 기반의 설정 파일들을 기반으로 테스트를 실행했다.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"classpath:config/WebConfig.xml", "classpath:config/web/SecurityConfigEx01.xml"})
@WebAppConfiguration
public class SecurityConfigEx01Test {
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
    
	@Test
	@DisplayName("SecurityConfigEx01에 등록한 필터 개수 확인")
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

	@Test
	public void testSecurityFilterChain02() {
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains()
				.getLast();
		List<Filter> filters = securityFilterChain.getFilters();
		
		for(Filter filter : filters) {
			System.out.println(filter.getClass().getSimpleName());
		}
		
		assertEquals(16, filters.size());
	}

	@Test
	public void testWebSecurity() throws Throwable {
		mvc.perform(get("/assets/images/logo.svg")).andExpect(status().isOk())
				.andExpect(content().contentType("image/svg+xml")).andDo(print());
	}

	/**
	 * SecurityConfigEx01.xml에서 /** 경로에 대해 모든 요청을 허용하도록 설정하여 바로 컨트롤러로 접근할 수 있다.
	 */
	@Test
	public void testHello() throws Throwable {
		mvc.perform(get("/ping")).andExpect(status().isOk())
				.andExpect(content().string("pong")).andDo(print());
	}
}
