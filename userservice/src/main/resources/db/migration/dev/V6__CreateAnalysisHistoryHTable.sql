DROP TABLE IF EXISTS user_service_db_clearskin.analysis_history_h;

CREATE TABLE user_service_db_clearskin.analysis_history_h (
                                                              analysis_history_h_id BIGINT NOT NULL AUTO_INCREMENT,
                                                              user_id BIGINT DEFAULT NULL,
                                                              severity VARCHAR(255) DEFAULT NULL,
                                                              analysis_time TIMESTAMP DEFAULT NULL,
                                                              modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                              PRIMARY KEY (analysis_history_h_id)
) ENGINE=InnoDB;