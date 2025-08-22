package com.clearskin_ai.userservice.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
