package com.clearskinai.adminservice.enums;

public enum AppointmentStatus {
    PENDING,      // Appointment request received but not yet confirmed
    CONFIRMED,    // Appointment has been confirmed
    CANCELLED,    // Appointment was cancelled by patient or admin
    COMPLETED,    // Appointment has taken place successfully
    RESCHEDULED   // Appointment has been rescheduled to a different time
}
