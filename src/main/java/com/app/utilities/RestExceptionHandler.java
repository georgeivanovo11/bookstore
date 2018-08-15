package com.app.utilities;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex)
	{
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex)
	{
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(CustomerNotFoundException.class)
    protected ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(BookNotFoundException.class)
    protected ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(PurchaseNotFoundException.class)
    protected ResponseEntity<Object> handlePurchaseNotFoundException(PurchaseNotFoundException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(BookNotAvailableException.class)
    protected ResponseEntity<Object> handleBookNotAvailableException(BookNotAvailableException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(PurchaseAlreadyPaidException.class)
    protected ResponseEntity<Object> handlePurchaseAlreadyPaidException(PurchaseAlreadyPaidException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(PurchaseAlreadyCanceledException.class)
    protected ResponseEntity<Object> handlePurchaseAlreadyCanceledException(PurchaseAlreadyCanceledException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(CustomerNotEnoughMoneyException.class)
    protected ResponseEntity<Object> handleCustomerNotEnoughMoneyException(CustomerNotEnoughMoneyException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(StoreNotEnoughMoneyException.class)
    protected ResponseEntity<Object> handleStoreNotEnoughMoneyException(StoreNotEnoughMoneyException ex)
	{
        ApiError apiError = new ApiError(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	

}
