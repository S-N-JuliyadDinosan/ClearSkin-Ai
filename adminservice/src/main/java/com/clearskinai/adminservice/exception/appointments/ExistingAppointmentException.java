package com.clearskinai.adminservice.exception.appointments;

public class ExistingAppointmentException extends RuntimeException {
    public ExistingAppointmentException(String message) {
        super(message);
    }
}
