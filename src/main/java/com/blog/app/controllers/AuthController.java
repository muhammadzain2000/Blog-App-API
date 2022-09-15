package com.blog.app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.models.User;
import com.blog.app.payloads.JwtAuthRequest;
import com.blog.app.payloads.JwtAuthResponse;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.UserRepo;
import com.blog.app.security.JwtTokenHelper;
import com.blog.app.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserRepo userRepo;
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(
			@RequestBody JwtAuthRequest request
			) throws Exception {
		
		this.authenticate(request.getUsername(), request.getPassword());
		System.out.println(request.getUsername());
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		System.out.println(token);
		
		JwtAuthResponse response= new JwtAuthResponse();
		response.setToken(token);
		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
		
		
	}


	private void authenticate(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		
		UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);

		} catch (BadCredentialsException e) {
			System.out.println("Invalid credential");
			throw new Exception("invalid username or password");
		}
		
	}
	
	//register new user api
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
		
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
		
		
		
	}
	
	
	@GetMapping("/token-expired/{token}")
	public ResponseEntity<JwtAuthResponse> isTokenExpiredEmployee(@PathVariable String token) throws Exception {
        Optional<User> user = null;
//        System.out.println(jwtTokenHelper.isTokenExpired(token));
        
            try {
				if (!jwtTokenHelper.isTokenExpired(token)) {
					return null;
				 
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(100);
			    String userName = "test@gmail.com";
			    user = userRepo.findByEmail(userName);
			   // System.out.println();
			    this.authenticate(userName, "test");
				
				 token = this.jwtTokenHelper.generateToken(user.get());
				System.out.println(token);
				
				JwtAuthResponse response= new JwtAuthResponse();
				response.setToken(token);

			    return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
			}
            return null;
       
      }

}
