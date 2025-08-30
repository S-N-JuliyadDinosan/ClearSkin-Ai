package com.clearskinai.adminservice.api.controller.advice;

import com.clearskinai.adminservice.constants.ApplicationConstants;
import com.clearskinai.adminservice.exception.appointments.*;
import com.clearskinai.adminservice.exception.doctor.DoctorAlreadyExistsException;
import com.clearskinai.adminservice.exception.doctor.DoctorNotFoundException;
import com.clearskinai.adminservice.util.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class AppointmentControllerExceptionHandler {

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessages> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorMessages> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "APPOINTMENT_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAppointmentStatusException.class)
    public ResponseEntity<ErrorMessages> handleInvalidAppointmentStatusException(InvalidAppointmentStatusException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "INVALID_APPOINTMENT_STATUS",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessages> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "INVALID_DATA",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessages> handleGeneralException(Exception ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExistingAppointmentException.class)
    public ResponseEntity<ErrorMessages> handleExistingAppointmentException(ExistingAppointmentException ex) {
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "EXISTING_APPOINTMENT",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CannotUpdateAppointmentException.class)
    public ResponseEntity<ErrorMessages> handleCannotUpdateAppointmentException(CannotUpdateAppointmentException ex){
        ErrorMessages errorMessages = new ErrorMessages(
                ex.getMessage(),
                "APPOINTMENT CANNOT BE UPDATED",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}