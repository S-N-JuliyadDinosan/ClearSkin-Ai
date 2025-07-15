DROP TABLE IF EXISTS doctor_h;

CREATE TABLE doctor_h (
                          doctor_h_id BIGINT NOT NULL AUTO_INCREMENT,
                          doctor_id BIGINT DEFAULT NULL,
                          name VARCHAR(255) DEFAULT NULL,
                          qualifications TEXT DEFAULT NULL,
                          speciality VARCHAR(255) DEFAULT NULL,
                          is_active INT DEFAULT NULL,
                          modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (doctor_h_id)
) ENGINE=InnoDB;
