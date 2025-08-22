DROP TRIGGER IF EXISTS user_service_db_clearskin.product_BEFORE_INSERT;
DROP TRIGGER IF EXISTS user_service_db_clearskin.product_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS user_service_db_clearskin.product_BEFORE_DELETE;

DELIMITER $$

USE user_service_db_clearskin$$

-- Create UPDATE trigger
CREATE DEFINER = CURRENT_USER TRIGGER product_BEFORE_UPDATE
BEFORE UPDATE ON product
                            FOR EACH ROW
BEGIN
INSERT INTO product_h (
    product_id,
    name,
    brand,
    product_link,
    added_date,
    modified_timestamp
) VALUES (
             OLD.product_id,
             OLD.name,
             OLD.brand,
             OLD.product_link,
             OLD.added_date,
             NOW()
         );
END$$

-- Create DELETE trigger
CREATE DEFINER = CURRENT_USER TRIGGER product_BEFORE_DELETE
BEFORE DELETE ON product
FOR EACH ROW
BEGIN
INSERT INTO product_h (
    product_id,
    name,
    brand,
    product_link,
    added_date,
    modified_timestamp
) VALUES (
             OLD.product_id,
             OLD.name,
             OLD.brand,
             OLD.product_link,
             OLD.added_date,
             NOW()
         );
END$$

DELIMITER ;