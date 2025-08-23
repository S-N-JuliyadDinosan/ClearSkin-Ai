package com.clearskin_ai.userservice.api.controller.advice;

import com.clearskin_ai.userservice.constants.ApplicationConstants;
import com.clearskin_ai.userservice.exception.InvalidCredentialsException;
import com.clearskin_ai.userservice.exception.PasswordMismatchException;
import com.clearskin_ai.userservice.exception.UserAlreadyExistsException;
import com.clearskin_ai.userservice.exception.UserNotFoundException;
import com.clearskin_ai.userservice.util.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessages> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                ApplicationConstants.USER_ALREADY_EXISTS,
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessages> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                ApplicationConstants.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorMessages> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                ApplicationConstants.INVALID_CREDENTIALS,
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorMessages> handlePasswordMismatchException(PasswordMismatchException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "Passwords do not match",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessages> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "Invalid request data",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}