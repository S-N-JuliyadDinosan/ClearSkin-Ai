package com.clearskinai.adminservice.api.controller.advice;

import com.clearskinai.adminservice.constants.ApplicationConstants;
import com.clearskinai.adminservice.exception.doctor.DoctorAlreadyExistsException;
import com.clearskinai.adminservice.exception.doctor.DoctorNotFoundException;
import com.clearskinai.adminservice.util.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class DoctorControllerExceptionHandler {

    @ExceptionHandler(DoctorAlreadyExistsException.class)
    public ResponseEntity<ErrorMessages> handleDoctorAlreadyExistsException(DoctorAlreadyExistsException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                ApplicationConstants.DOCTOR_ALREADY_EXISTS,
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorMessages> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                ApplicationConstants.DOCTOR_NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessages> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "Invalid doctor data",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessages> handleGeneralException(Exception ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
