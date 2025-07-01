package com.clearskin_ai.adminservice.constants;

public interface ApplicationConstants {

    // Appointment Messages
    String APPOINTMENT_NOT_FOUND = "Appointment not found";
    String APPOINTMENT_ALREADY_EXISTS = "Appointment already exists with given identifier";
    String INVALID_APPOINTMENT_ID = "Invalid appointment ID";
    String APPOINTMENT_CREATED_SUCCESSFULLY = "Appointment created successfully";
    String APPOINTMENT_UPDATED_SUCCESSFULLY = "Appointment updated successfully";
    String APPOINTMENT_DELETED_SUCCESSFULLY = "Appointment deleted successfully";
    String APPOINTMENT_CANCELLED_SUCCESSFULLY = "Appointment cancelled successfully";
    String APPOINTMENT_CONFIRMED_SUCCESSFULLY = "Appointment confirmed successfully";
    String APPOINTMENT_COMPLETED_SUCCESSFULLY = "Appointment marked as completed successfully";
    String APPOINTMENT_RESCHEDULED_SUCCESSFULLY = "Appointment rescheduled successfully";

    // Validation Errors
    String INVALID_DOCTOR_ID = "Invalid doctor ID";
    String INVALID_USER_ID = "Invalid user ID";
    String APPOINTMENT_DATE_IN_PAST = "Appointment date cannot be in the past";
    String APPOINTMENT_TIME_CONFLICT = "Appointment time conflicts with another appointment";
    String APPOINTMENT_STATUS_INVALID = "Invalid appointment status";

    // Statuses (if needed as constants, though enum is better)
    String STATUS_PENDING = "PENDING";
    String STATUS_CONFIRMED = "CONFIRMED";
    String STATUS_CANCELLED = "CANCELLED";
    String STATUS_COMPLETED = "COMPLETED";
    String STATUS_NO_SHOW = "NO_SHOW";
    String STATUS_RESCHEDULED = "RESCHEDULED";

}
