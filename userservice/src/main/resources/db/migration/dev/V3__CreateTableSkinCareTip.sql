DROP TABLE IF EXISTS `skincare_tip`;

CREATE TABLE skincare_tip (
                              tip_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              category VARCHAR(255),
                              title VARCHAR(255),
                              content TEXT
);
