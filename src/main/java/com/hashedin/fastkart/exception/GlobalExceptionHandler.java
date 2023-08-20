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

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@ControllerAdvice
@Slf4j
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
	
	
	
	 /**
     * Handles the exception when an HTTP request method is not supported.
     * Returns a response with a "HTTP Method Not Supported" error message.
     *
     * @param ex The exception indicating the unsupported HTTP method.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HTTP Method Not Supported: {}", ex.getMessage());
        ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.METHOD_NOT_ALLOWED.toString(),
            "HTTP Method Not Supported",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    
    
    /**
     * Handles the exception when the request body is not readable or malformed.
     * Returns a response with a "Malformed Request Body" error message.
     *
     * @param ex The exception indicating a malformed request body.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Malformed Request Body: {}", ex.getMessage());
        ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.BAD_REQUEST.toString(),
            "Malformed Request Body",
            ex.getMessage().substring(0, ex.getMessage().indexOf(":"))
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    

    /**
     * Handles the exception when the media type of the request is not supported.
     * Returns a response with an "Unsupported Media Type" error message.
     *
     * @param ex The exception indicating the unsupported media type.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("Unsupported Media Type: {}", ex.getMessage());
        ErrorResponse errorDetails = new ErrorResponse(
            new Date(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(),
            "Unsupported Media Type",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    
    
    /**
     * Handles the exception when a method argument type mismatch occurs.
     * Returns a response with a "Bad Request" error message indicating the invalid parameter type.
     *
     * @param ex The exception indicating the method argument type mismatch.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Bad Request - Invalid parameter type: {}", ex.getName());
        ErrorResponse errorResponse = new ErrorResponse(
            new Date(),
            HttpStatus.BAD_REQUEST.toString(),
            "Bad Request",
            "Invalid parameter type: " + ex.getName()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    

    /**
     * Handles the exception when a request is rejected, typically due to security constraints.
     * Returns a response with a "Forbidden" error message indicating the request rejection reason.
     *
     * @param ex The exception indicating the request rejection.
     * @return A ResponseEntity with an ErrorResponse and HTTP status code.
     */
    @ExceptionHandler(RequestRejectedException.class)
    public ResponseEntity<ErrorResponse> handleRequestRejectedException(RequestRejectedException ex) {
        log.error("Forbidden - Request rejected: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            new Date(),
            HttpStatus.FORBIDDEN.toString(),
            "Forbidden",
            "Request rejected: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
	
}