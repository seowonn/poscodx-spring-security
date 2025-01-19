package filter;

import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// GenericFilterBena을 상속하여 구현한 커스텀 필터 
public class RealFilter extends GenericFilterBean {

	/**
	 * doFilter 메서드를 구현하여, HTTP 요청 및 응답을 처리하기 전에,
	 * 특정 작업을 수행하고, 이후 필터 체인을 계속 진행하도록 설정한다. 
	 * 작업: 브라우저에 특정 쿠키를 전달하는 역할
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Cookie cookie = new Cookie("RealFilter", "Works");
		cookie.setPath(((HttpServletRequest)request).getContextPath());
		cookie.setMaxAge(60);
		
		((HttpServletResponse) response).addCookie(cookie);
		chain.doFilter(request, response); // 필터 체인의 다음 단계(ex. 컨트롤러)로 요청을 전달 (없으면 멈춤)
	}

}
