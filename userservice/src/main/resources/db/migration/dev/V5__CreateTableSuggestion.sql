DROP TABLE IF EXISTS `suggestion`;

CREATE TABLE suggestion (
                            suggestion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            severity VARCHAR(255),
                            text TEXT
);
