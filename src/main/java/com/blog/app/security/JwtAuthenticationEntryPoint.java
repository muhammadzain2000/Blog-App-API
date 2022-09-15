package com.blog.app.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private HttpSession httpSession;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		//jab koi authorized access hoga to yaha se respose clinet ko jayga
		String message = (httpSession.getAttribute("newToken")) != null ? (String)httpSession.getAttribute("newToken") : "Access denied";
		System.out.println("NEW TOKEN : " + message);
		response.sendError(HttpServletResponse.SC_OK, message);

	}

}
