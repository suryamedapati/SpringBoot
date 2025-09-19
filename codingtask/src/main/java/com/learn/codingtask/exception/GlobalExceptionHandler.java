package com.learn.codingtask.exception;

import com.learn.codingtask.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ApiResponse<Map<String, String>> response = new ApiResponse<>(false, "Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.EmployeeNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEmployeeNotFound(CustomExceptions.EmployeeNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUsernameExists(CustomExceptions.UsernameAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomExceptions.EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUsernameExists(CustomExceptions.EmailAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralError(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "An error occurred: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomExceptions.NoExistingLeaveRequestException.class)
    public ResponseEntity<ApiResponse<String>> handleNoLeaveRequests(CustomExceptions.NoExistingLeaveRequestException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.NoLeaveRequestForEmployeeException.class)
    public ResponseEntity<ApiResponse<String>> handleNoLeaveRequestForEmployee(CustomExceptions.NoLeaveRequestForEmployeeException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.LeaveNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleLeaveNotFound(CustomExceptions.LeaveNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.InvalidStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidStatus(CustomExceptions.InvalidStatusException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(CustomExceptions.UnauthorizedActionException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorized(CustomExceptions.UnauthorizedActionException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedEmployees(CustomExceptions.UnauthorizedException ex){
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    public ResponseEntity<ApiResponse<String>> handleInactiveProfileException(CustomExceptions.InactiveProfileException ex){
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

