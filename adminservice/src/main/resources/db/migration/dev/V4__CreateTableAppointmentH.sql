DROP TABLE IF EXISTS appointment_h;

CREATE TABLE appointment_h (
                               appointment_h_id BIGINT NOT NULL AUTO_INCREMENT,
                               appointment_id BIGINT DEFAULT NULL,
                               user_id BIGINT DEFAULT NULL,
                               user_email VARCHAR(255) DEFAULT NULL,
                               user_name VARCHAR(255) DEFAULT NULL,
                               date DATETIME DEFAULT NULL,
                               doctor_id BIGINT DEFAULT NULL,
                               status VARCHAR(50) DEFAULT NULL,
                               is_active INT DEFAULT NULL,
                               modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (appointment_h_id)
) ENGINE=InnoDB;
