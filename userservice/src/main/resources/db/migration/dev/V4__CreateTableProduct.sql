DROP TABLE IF EXISTS `product`;

CREATE TABLE product (
                         product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         brand VARCHAR(255),
                         product_link TEXT,
                         added_date TIMESTAMP
);
