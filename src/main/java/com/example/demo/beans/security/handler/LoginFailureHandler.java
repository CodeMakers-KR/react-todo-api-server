package com.example.demo.beans.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, 
										HttpServletResponse response,
										AuthenticationException exception) 
							throws IOException, ServletException {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/member/memberlogin.jsp");
		request.setAttribute("message", exception.getMessage());
		rd.forward(request, response);
	}

}
