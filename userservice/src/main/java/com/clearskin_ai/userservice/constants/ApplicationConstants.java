package com.clearskin_ai.userservice.constants;

public interface ApplicationConstants {

    // User Messages
    String USER_NOT_FOUND = "User not found";
    String USER_ALREADY_EXISTS = "User already exists with given identifier";
    String INVALID_USER_ID = "Invalid user ID";
    String COMMON_USER_CREATED_SUCCESSFULLY = "User created successfully";
    String ADMIN_USER_CREATED_SUCCESSFULLY = "User created successfully , Check the Email to get the Password";
    String USER_UPDATED_SUCCESSFULLY = "User updated successfully";
    String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    String USER_LOGIN_SUCCESSFUL = "User login successful";
    String USER_LOGOUT_SUCCESSFUL = "User logout successful";

    // Validation Errors
    String INVALID_EMAIL = "Invalid email address";
    String INVALID_PASSWORD = "Invalid password";
    String EMAIL_ALREADY_EXISTS = "Email is already registered";
    String MISSING_REQUIRED_FIELDS = "Missing required user fields";
    String INVALID_CREDENTIALS = "Invalid email address or password";

    // Statuses (if needed as constants, though enum is better)
    String STATUS_ACTIVE = "ACTIVE";
    String STATUS_INACTIVE = "INACTIVE";
    String STATUS_BLOCKED = "BLOCKED";

    // Products
    String PRODUCT_ALREADY_EXISTS = "PRODUCT_ALREADY_EXISTS";
    String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";


    // Email
    String EMAIL_SUBJECT_WELCOME = "Welcome to ClearSkin AI!";
    String EMAIL_BODY_TEMPLATE = "Hi %s,\n\nWelcome to ClearSkin AI!\nYour account has been created successfully.\n\nUsername: %s\nPassword: %s\n\nPlease change your password after logging in.\n\nBest regards,\nClearSkin Team";
    String EMAIL_BODY_TEMPLATE_USER = "Dear %s,\n\nWelcome to ClearSkin AI! Your account has been created.\nEmail: %s\n\nLogin at http://localhost:3000/login";
    String EMAIL_BODY_TEMPLATE_ADMIN = "Dear %s,\n\nWelcome to ClearSkin AI! Your account has been created.\nEmail: %s\nPassword: %s\n\nLogin at http://localhost:3000/login";
}
