package com.clearskinai.adminservice.exception.appointments;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
