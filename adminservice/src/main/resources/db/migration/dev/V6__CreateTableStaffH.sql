DROP TABLE IF EXISTS staff_h;

CREATE TABLE staff_h (
                         staff_h_id BIGINT NOT NULL AUTO_INCREMENT,
                         staff_id BIGINT DEFAULT NULL,
                         email VARCHAR(255) DEFAULT NULL,
                         password VARCHAR(255) DEFAULT NULL,
                         role VARCHAR(50) DEFAULT NULL,
                         is_active INT DEFAULT NULL,
                         modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (staff_h_id)
) ENGINE=InnoDB;
