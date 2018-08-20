package com.app.utilities;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends   DefaultHandlerExceptionResolver {
	
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
	
	@ExceptionHandler(InvalidInputDataException.class)
    protected ResponseEntity<Object> handleInvalidInputDataException(InvalidInputDataException ex)
	{
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex)
	{
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.status);
    }
	
	@ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(Throwable ex) {
        return new ResponseEntity<Object>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	/////////////////////////////////////////////////////

	
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
