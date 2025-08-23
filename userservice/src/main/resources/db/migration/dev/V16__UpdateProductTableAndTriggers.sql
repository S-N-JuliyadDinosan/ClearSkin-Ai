-- Drop existing tables/triggers (if needed)
DROP TRIGGER IF EXISTS product_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS product_BEFORE_DELETE;

DROP TABLE IF EXISTS product_h;
DROP TABLE IF EXISTS product;

-- Create main product table
CREATE TABLE product (
                         product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         brand VARCHAR(255),
                         product_link TEXT,
                         added_date TIMESTAMP,
                         product_image_link TEXT,
                         product_description TEXT,
                         skin_type VARCHAR(255)
) ENGINE=InnoDB;

-- Create product history table
CREATE TABLE product_h (
                           product_h_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           product_id BIGINT DEFAULT NULL,
                           name VARCHAR(255) DEFAULT NULL,
                           brand VARCHAR(255) DEFAULT NULL,
                           product_link TEXT DEFAULT NULL,
                           added_date TIMESTAMP DEFAULT NULL,
                           product_image_link TEXT DEFAULT NULL,
                           product_description TEXT DEFAULT NULL,
                           skin_type VARCHAR(255) DEFAULT NULL,
                           modified_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Create UPDATE trigger
CREATE TRIGGER product_BEFORE_UPDATE
    BEFORE UPDATE ON product
    FOR EACH ROW
BEGIN
    INSERT INTO product_h (
        product_id,
        name,
        brand,
        product_link,
        added_date,
        product_image_link,
        product_description,
        skin_type,
        modified_timestamp
    )
    VALUES (
               OLD.product_id,
               OLD.name,
               OLD.brand,
               OLD.product_link,
               OLD.added_date,
               OLD.product_image_link,
               OLD.product_description,
               OLD.skin_type,
               NOW()
           );
END;

-- Create DELETE trigger
CREATE TRIGGER product_BEFORE_DELETE
    BEFORE DELETE ON product
    FOR EACH ROW
BEGIN
    INSERT INTO product_h (
        product_id,
        name,
        brand,
        product_link,
        added_date,
        product_image_link,
        product_description,
        skin_type,
        modified_timestamp
    )
    VALUES (
               OLD.product_id,
               OLD.name,
               OLD.brand,
               OLD.product_link,
               OLD.added_date,
               OLD.product_image_link,
               OLD.product_description,
               OLD.skin_type,
               NOW()
           );
END;
