package com.example.demo.beans.security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.beans.security.SecurityUser;
import com.example.demo.member.vo.MemberVO;
import com.example.demo.util.AjaxResponse;
import com.example.demo.util.StringUtil;
import com.google.gson.Gson;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JsonWebTokenProvider jwtProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
								    HttpServletResponse response, 
								    FilterChain filterChain)
			throws ServletException, IOException {
		
		String servletPath = request.getServletPath();
		String method = request.getMethod();
		
		System.out.println("Path: " + servletPath);
		
		// 요청 URL이 "/api/"로 시작하는 경우만 처리.
		if ( servletPath.startsWith("/api/v1/")) {
			
			
			
			if (CorsUtils.isPreFlightRequest(request)
					|| (servletPath.matches("^/api/v[0-9]/auth/token$") && method.equalsIgnoreCase("POST"))
					|| (servletPath.matches("^/api/v[0-9]/member$") && method.equalsIgnoreCase("POST"))
					|| (servletPath.contains("/member/available/"))) {
				System.out.println("Pass Path: " + servletPath);
				
				filterChain.doFilter(request, response);
				return;
			}
			else {
				System.out.println("Check Path: " + servletPath);
				// HttpReqeust Header에 추가된 Authorization 값을 추출.
				String jwt = request.getHeader("Authorization");
				
				if (StringUtil.isEmpty(jwt)) {
					forceSendResponse(response, "인증이 필요합니다.");
					return;
				}
				
				MemberVO member = null;
				try {
					// 토큰에서 MemberVO 객체를 가져온다.
					member = jwtProvider.getUserFromToken(jwt);
				}
				catch(SignatureException se) {
					forceSendResponse(response, "인증 정보가 잘못되었습니다.");
					return;
				}
				SecurityUser user = new SecurityUser(member);
				
				// 인증과 권한 정보 생성
				Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				
				// Security Context에 인증정보를 셋팅한다.
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		filterChain.doFilter(request, response);			
	}
	
	private void forceSendResponse(HttpServletResponse response, String message) throws IOException {
		AjaxResponse ajaxResponse = new AjaxResponse(HttpStatus.UNAUTHORIZED);
		ajaxResponse.setErrorMessage(message);
		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type", "application/json");
		response.getWriter().write(new Gson().toJson(ajaxResponse));
		response.getWriter().flush();
		response.getWriter().close();
	}
}
