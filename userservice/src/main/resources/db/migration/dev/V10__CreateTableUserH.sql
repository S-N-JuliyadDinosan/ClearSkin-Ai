DROP TABLE IF EXISTS user_service_db_clearskin.users_h;

CREATE TABLE user_service_db_clearskin.users_h (
                                                   user_h_id BIGINT NOT NULL AUTO_INCREMENT,
                                                   user_id BIGINT DEFAULT NULL,
                                                   email VARCHAR(255) DEFAULT NULL,
                                                   password VARCHAR(255) DEFAULT NULL,
                                                   name VARCHAR(255) DEFAULT NULL,
                                                   created_at TIMESTAMP DEFAULT NULL,
                                                   modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                   PRIMARY KEY (user_h_id)
) ENGINE=InnoDB;