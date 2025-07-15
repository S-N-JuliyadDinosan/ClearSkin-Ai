DROP TABLE IF EXISTS `analysis_history`;

CREATE TABLE analysis_history (
                                  history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_id BIGINT,
                                  severity VARCHAR(255),
                                  analysis_time TIMESTAMP,
                                  FOREIGN KEY (user_id) REFERENCES users(user_id)
);
