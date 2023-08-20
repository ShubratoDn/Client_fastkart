package com.hashedin.fastkart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Resource not found exception response entity.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
//	@ExceptionHandler(JWTAPIException.class)
//	public ResponseEntity<?> resourceNotFoundException(JWTAPIException ex, WebRequest request) {
//		ErrorResponse errorDetails = new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(), ex.getMessage(),
//				request.getDescription(false));
//		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//	}

	/**
	 * Globle excpetion handler response entity.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
//		ErrorResponse errorDetails = new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
//				ex.getMessage(), request.getDescription(false));
//		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		  ErrorResponse errorDetails = new ErrorResponse(
			        new Date(),
			        HttpStatus.METHOD_NOT_ALLOWED.toString(),
			        "HTTP Method Not Supported",
			        ex.getMessage() 
			    );	
		  return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {	
		   ErrorResponse errorDetails = new ErrorResponse(
			        new Date(),
			        HttpStatus.BAD_REQUEST.toString(),
			        "Malformed Request Body",
			        ex.getMessage().substring(0, ex.getMessage().indexOf(":")) 
			    );		
		  return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(),
            "Unsupported Media Type",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
	
	
	 @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
	        ErrorResponse errorResponse = new ErrorResponse(
	            new Date(),
	            HttpStatus.BAD_REQUEST.toString(),
	            "Bad Request",
	            "Invalid parameter type: " + ex.getName()
	        );

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	    }
	 
	 
	 @ExceptionHandler(RequestRejectedException.class)
    public ResponseEntity<ErrorResponse> handleRequestRejectedException(RequestRejectedException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
            new Date(),
            HttpStatus.FORBIDDEN.toString(),
            "Forbidden",
            "Request rejected: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
	
}