package filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import config.AppConfig;
import config.WebConfig;
import jakarta.servlet.Filter;

/**
 * 클래스 레벨 어노테이션: 클래스 위에 붙이는 어노테이션 @ExtendWith(SpringExtension.class): Spring
 * Application Context를 테스트 환경에서 사용할 수 있게 만든다.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class, AppConfig.class })
//mvc 환경을 구성한다.
@WebAppConfiguration
public class FilterTest {

	// 사용자가 임의 구성하는 톰켓
	private static MockMvc mvc;

	@BeforeAll
	public static void setup(WebApplicationContext applicationContext) {
		/**
		 * Spring 컨테이너를 넣어 Web Application 환경을 모의 설정한다.
		 */
		Filter filter = (Filter) applicationContext.getBean("realFilter");
		mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.addFilter(new DelegatingFilterProxy(filter), "/*").build();
	}

	@Test
	public void testHello() throws Throwable {
		mvc.perform(get("/hello")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(cookie().value("RealFilter", "Works"))
				.andExpect(MockMvcResultMatchers.content().string("Hello World"))
				.andDo(MockMvcResultHandlers.print());
	}

}
