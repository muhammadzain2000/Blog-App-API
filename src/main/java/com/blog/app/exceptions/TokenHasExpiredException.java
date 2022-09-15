package com.blog.app.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenHasExpiredException extends RuntimeException{

	String message;
	String newToken;
	long fieldValue;
	public TokenHasExpiredException(String message, String newToken) {
//		super(String.format("not found with %s : %s", message,newToken));
		this.message = message;
		this.newToken = newToken;
	}
}
