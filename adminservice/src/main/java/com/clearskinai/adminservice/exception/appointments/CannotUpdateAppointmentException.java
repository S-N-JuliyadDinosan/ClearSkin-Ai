package com.clearskinai.adminservice.exception.appointments;

public class CannotUpdateAppointmentException extends RuntimeException{
    public CannotUpdateAppointmentException(String message){
        super(message);
    }
}
