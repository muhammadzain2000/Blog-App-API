package com.blog.app.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blog.app.exceptions.GlobalExceptionHandler;
import com.blog.app.exceptions.TokenHasExpiredException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private HttpSession httpSession;
	
	GlobalExceptionHandler globalExceptionHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

//		1. get token 
		
		//token ko header mai authorization key mai bhejenge
		String requestToken = request.getHeader("Authorization");
		Enumeration<String> headerNames = request.getHeaderNames();

		while(headerNames.hasMoreElements())
		{
			System.out.println(headerNames.nextElement());
		}
		// Bearer 2352523sdgsg, this is how token starts

		System.out.println(requestToken);

		String username = null;

		String token = null;

		if (requestToken != null && requestToken.startsWith("Bearer")) {
			
			//this is token without bearer i.e. actual token
			token = requestToken.substring(7);
			System.out.println(token);
			System.out.println("* HERE******");

			try {
				username = this.jwtTokenHelper.getUsernameFromToken(token);
				System.out.println("Username expired : " + username);
				
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get Jwt token");

			} catch (ExpiredJwtException e) {
				System.out.println("Jwt token has expired");
				username = (String) httpSession.getAttribute("username");
				Map<String, Object> claims = new HashMap<>();
				String newToken = this.jwtTokenHelper.doGenerateToken(claims, username);
				httpSession.setAttribute("newToken", newToken);
				System.out.println("NEW TOKEN : " + newToken);
				throw new ExpiredJwtException(null, null, "Your token has expired! This is the new generated token: "+newToken);

			} catch (MalformedJwtException e) {
				System.out.println("invalid jwt");

			}

		} else {
			System.out.println("Jwt token does not begin with Bearer");
		}

		// once we get the token , now validate

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (this.jwtTokenHelper.validateToken(token, userDetails)) {
				// shi chal rha hai
				// authentication karna hai

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				System.out.println("Invalid jwt token");
			}

		} else {
			System.out.println("username is null or context is not null");
		}

		
		filterChain.doFilter(request, response);
	}

}