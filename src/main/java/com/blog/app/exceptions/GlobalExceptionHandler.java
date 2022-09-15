package com.blog.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.app.payloads.ApiResponse;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
		// data hash map mai rakhenge
		Map<String, String> response = new HashMap<>();

		// traversing every error of fields
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			// typecasting error to field error
			String fieldName = ((FieldError) error).getField();

			String message = error.getDefaultMessage();
			response.put(fieldName, message);
		});

		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

//	@ExceptionHandler(ExpiredJwtException.class)
//	public ResponseEntity<Map<String, String>> unauthorized(ExpiredJwtException ex) {
//
//		Map<String, String> response = new HashMap<>();
//
//		response.put("message", "Token has expired");
////		response.put("newToken", ex.getNewToken());
//
//
//		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
//	}

	

}
