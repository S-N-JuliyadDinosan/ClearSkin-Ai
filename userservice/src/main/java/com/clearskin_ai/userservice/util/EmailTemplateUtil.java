package com.clearskin_ai.userservice.util;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateUtil {

    private static final String BASE_HTML_START = "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<style>"
            + "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }"
            + "  .container { background-color: #ffffff; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }"
            + "  .header { text-align: center; padding-bottom: 20px; }"
            + "  .header h1 { color: #6c63ff; }"
            + "  .content { font-size: 16px; line-height: 1.6; }"
            + "  .credentials { background-color: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0; font-family: monospace; }"
            + "  .footer { text-align: center; font-size: 14px; color: #888888; padding-top: 20px; }"
            + "  a.button { display: inline-block; padding: 10px 20px; margin-top: 10px; background-color: #6c63ff; color: #ffffff; text-decoration: none; border-radius: 5px; }"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<div class='container'>"
            + "  <div class='header'>"
            + "    <h1>%s</h1>"
            + "  </div>"
            + "  <div class='content'>"
            + "    %s"
            + "  </div>"
            + "  <div class='footer'>"
            + "    <p>ClearSkin AI &copy; 2025. All rights reserved.</p>"
            + "  </div>"
            + "</div>"
            + "</body>"
            + "</html>";

    /**
     * Returns a complete HTML email using title and content.
     *
     * @param title   The email header/title
     * @param content The main content of the email in HTML format
     * @return formatted HTML email string
     */
    public String buildEmail(String title, String content) {
        return String.format(BASE_HTML_START, title, content);
    }

    /**
     * Creates the credential section for emails (username + password)
     */
    public String buildCredentialsSection(String email, String password) {
        return "<p>Your account has been created successfully. You can now log in and get started.</p>"
                + "<div class='credentials'>"
                + "<p><strong>Email:</strong> " + email + "</p>"
                + "<p><strong>Password:</strong> " + password + "</p>"
                + "</div>"
                + "<p>Click the button below to login:</p>"
                + "<a class='button' href='http://localhost:8000/login'>Login to ClearSkin AI</a>"
                + "<p>If you didn't request this, please ignore this email.</p>";
    }
}
