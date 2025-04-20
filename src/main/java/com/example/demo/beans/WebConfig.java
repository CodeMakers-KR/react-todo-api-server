package com.example.demo.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Configurable
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	/**
	 * Static Resources 설정도 함께 해주어야 한다.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**")
					.addResourceLocations("classpath:/static/js/");
	}
	
	/**
	 * 인터셉터를 등록한다.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 세션 체크를 하지 않을 URL 패턴 정의
		List<String> excludePatterns = new ArrayList<>();
		excludePatterns.add("/member/regist/**");
		excludePatterns.add("/member/login");
		
		// 인터셉터 등록하기.
		registry.addInterceptor(new SecurityAuthenticationInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(excludePatterns);
	}
	
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/api/v1/**")
//				.allowedOrigins("http://localhost:3000")
//				.allowedMethods("FETCH", "OPTIONS", "POST", "GET", "PUT", "DELETE");
//	}
	
}
