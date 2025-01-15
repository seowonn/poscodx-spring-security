package initializer;

import config.AppConfig;
import config.WebConfig;
import jakarta.servlet.Filter;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @NonNull
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

	@Override
	protected Filter[] getServletFilters() {
		/**
		 * AppConfig에 @Bean으로 등록해놓은 메서드를 등록한다. 이때 이름과 메서드명 일치!
		 */
		return new Filter[] { new DelegatingFilterProxy("realFilter") };
	}    
    
}