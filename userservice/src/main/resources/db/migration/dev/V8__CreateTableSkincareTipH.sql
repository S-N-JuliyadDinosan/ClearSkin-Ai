DROP TABLE IF EXISTS user_service_db_clearskin.skincare_tip_h;

CREATE TABLE user_service_db_clearskin.skincare_tip_h (
                                                          tip_h_id BIGINT NOT NULL AUTO_INCREMENT,
                                                          tip_id BIGINT DEFAULT NULL,
                                                          category VARCHAR(255) DEFAULT NULL,
                                                          title VARCHAR(255) DEFAULT NULL,
                                                          content TEXT DEFAULT NULL,
                                                          modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                          PRIMARY KEY (tip_h_id)
) ENGINE=InnoDB;