package com.finstack.projectlog.aspect;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.finstack.projectlog.exception.ErrorResponse;
import com.finstack.projectlog.exception.UnauthorizedException;

@ControllerAdvice
public class ExceptionAspect {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> unauthorizedExceptionHandler(UnauthorizedException exception){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setErrorMessage(exception.getMessage());
		errorResponse.setDescription(exception.getLocalizedMessage());
		errorResponse.setErrorStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setErrorStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setErrorMessage(exception.getMessage());
		errorResponse.setDescription(exception.getLocalizedMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    
    @ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> htpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setErrorStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrorMessage(exception.getMessage());
		errorResponse.setDescription(exception.getLocalizedMessage());

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}
    
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setErrorMessage(exception.getMessage());
		errorResponse.setDescription(exception.getLocalizedMessage());

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
