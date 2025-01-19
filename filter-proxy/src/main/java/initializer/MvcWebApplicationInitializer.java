package initializer;

import config.AppConfig;
import config.WebConfig;
import jakarta.servlet.Filter;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * MvcWebApplicationInitializer 동작
 * WebApplicationInitializer를 AbstractAnnotationConfigDispatcherServletInitializer가
 * 구현하고 있기 때문에 Spring에서 자동으로 감지하고 가장 먼저 실행된다. 
 */
public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

//	Root Application Context의 설정 클래스로 사용할 클래스를 지정한다. 
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

//  DispatcherServlet(Application Context)의 설정 클래스를 지정한다.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

//  Dispatcher Servlet의 매핑 경로를 지정한다.
    @NonNull
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

	/**
	 * DispatcherServlet의 앞단에 적용할 필터를 지정한다. 
	 * AppConfig에 @Bean으로 등록해놓은 메서드를 등록한다. 이때 이름과 메서드명 일치!
	 */
	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new DelegatingFilterProxy("realFilter") };
	}    
    
}