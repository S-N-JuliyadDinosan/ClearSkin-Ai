DROP TABLE IF EXISTS user_service_db_clearskin.product_h;

CREATE TABLE user_service_db_clearskin.product_h (
                                                     product_h_id BIGINT NOT NULL AUTO_INCREMENT,
                                                     product_id BIGINT DEFAULT NULL,
                                                     name VARCHAR(255) DEFAULT NULL,
                                                     brand VARCHAR(255) DEFAULT NULL,
                                                     product_link TEXT DEFAULT NULL,
                                                     added_date TIMESTAMP DEFAULT NULL,
                                                     modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                     PRIMARY KEY (product_h_id)
) ENGINE=InnoDB;