package com.example.demo.beans.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.demo.beans.security.handler.LoginFailureHandler;
import com.example.demo.beans.security.jwt.JwtAuthenticationFilter;
import com.example.demo.member.dao.MemberDao;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
//	@Autowired
//	private OAuthService oauthService;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new SecurityUserDetailsService(memberDao);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SecuritySHA();
	}
	
	/**
	 * 인증 체크를 제외할 URL 정의
	 * 여기에 등록된 URL들은 Spring Security가 절대 개입하지 않는다.
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring().requestMatchers(AntPathRequestMatcher.antMatcher("/WEB-INF/views/**"))
	    							//.requestMatchers("/member/login") 
	    							  .requestMatchers(AntPathRequestMatcher.antMatcher("/member/regist/**"))
	    							  .requestMatchers(AntPathRequestMatcher.antMatcher("/error/**"))
	    							  .requestMatchers(AntPathRequestMatcher.antMatcher("/favicon.ico"))
	    							  .requestMatchers(AntPathRequestMatcher.antMatcher("/member/**-delete-me"))
	    							  .requestMatchers(AntPathRequestMatcher.antMatcher("/js/**"));
	}
	
	/**
	 * Spring Security Filter가 동작해야할 방식(순서)을 정의
	 * @param http HttpSecurity 필터 전략
	 * @return SpringSecurityFilterChain (Spring Security가 동작해야할 순서)
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// Spring Security의 BasicAuthenticationFilter 다음에 JwtAuthenticationFilter를 추가한다.
		http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
		
		http.authorizeHttpRequests(httpRequest ->
										// /board/list 는 인증 여부와 관계없이 모두 접근이 가능하다.
										httpRequest.requestMatchers(AntPathRequestMatcher.antMatcher("/board/list")).permitAll()
												   .requestMatchers(AntPathRequestMatcher.antMatcher("/member/login")).permitAll()
												   .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/token")).permitAll()
												   .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/member")).permitAll()
												   .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/member/available/**")).permitAll()
												   .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/member/logout")).permitAll()
												   // /board/excel/download는 "ADMIN" 권한만 접근이 가능하다.
												   .requestMatchers(AntPathRequestMatcher.antMatcher("/board/excel/download")).hasRole("ADMIN")
												   // 그외 모든 URL은 인증이 필요하다.
												   .anyRequest().authenticated());
		
		
		// OAuth Login 및 후 처리 설정.
//		http.oauth2Login(auth -> auth.defaultSuccessUrl("/board/list", true)
//									 .userInfoEndpoint(user -> user.userService(oauthService))
//									 .loginPage("/member/login"));
		
		
		// CSRF 방어를 적용한다
		http.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/**")));
		
		http.cors(cors -> {
			CorsConfigurationSource corsConfigurationSource = request -> {
				CorsConfiguration corsConfiguration = new CorsConfiguration();
				corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
				corsConfiguration.setAllowedMethods(List.of("OPTIONS", "FETCH", "POST", "PUT", "GET", "DELETE"));
				corsConfiguration.addAllowedHeader("*");
				return corsConfiguration;
			};
			
			cors.configurationSource(corsConfigurationSource);
		});
		
		// 로그인에 성공하면 "/board/list"로 이동하도록 함.
		http.formLogin(formLogin -> 
			formLogin.disable()
//				formLogin.defaultSuccessUrl("/board/list")
//						 .failureHandler(new LoginFailureHandler())
//						 .loginPage("/member/login")
//						 .loginProcessingUrl("/member/login-proc")
//						 .usernameParameter("email")
//						 .passwordParameter("password")
						 );
		return http.build();
	}
	
}