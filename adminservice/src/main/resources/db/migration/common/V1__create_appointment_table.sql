CREATE TABLE appointment (
    appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    user_email VARCHAR(255),
    user_name VARCHAR(255),
    date DATETIME,
    doctor_id BIGINT,
    status VARCHAR(50)
);
