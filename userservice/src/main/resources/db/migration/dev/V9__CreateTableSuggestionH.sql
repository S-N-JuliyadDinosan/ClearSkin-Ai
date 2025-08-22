DROP TABLE IF EXISTS user_service_db_clearskin.suggestion_h;

CREATE TABLE user_service_db_clearskin.suggestion_h (
                                                        suggestion_h_id BIGINT NOT NULL AUTO_INCREMENT,
                                                        suggestion_id BIGINT DEFAULT NULL,
                                                        severity VARCHAR(255) DEFAULT NULL,
                                                        text TEXT DEFAULT NULL,
                                                        modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                        PRIMARY KEY (suggestion_h_id)
) ENGINE=InnoDB;