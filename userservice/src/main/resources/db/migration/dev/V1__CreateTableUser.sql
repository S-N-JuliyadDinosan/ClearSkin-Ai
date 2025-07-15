DROP TABLE IF EXISTS `users`;

CREATE TABLE users (
                      user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      name VARCHAR(255),
                      created_at TIMESTAMP
);
