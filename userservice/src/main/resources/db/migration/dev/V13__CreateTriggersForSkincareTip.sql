DROP TRIGGER IF EXISTS user_service_db_clearskin.skincare_tip_BEFORE_INSERT;
DROP TRIGGER IF EXISTS user_service_db_clearskin.skincare_tip_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS user_service_db_clearskin.skincare_tip_BEFORE_DELETE;

DELIMITER $$

USE user_service_db_clearskin$$

-- Create UPDATE trigger
CREATE DEFINER = CURRENT_USER TRIGGER skincare_tip_BEFORE_UPDATE
BEFORE UPDATE ON skincare_tip
                            FOR EACH ROW
BEGIN
INSERT INTO skincare_tip_h (
    tip_id,
    category,
    title,
    content,
    modified_timestamp
) VALUES (
             OLD.tip_id,
             OLD.category,
             OLD.title,
             OLD.content,
             NOW()
         );
END$$

-- Create DELETE trigger
CREATE DEFINER = CURRENT_USER TRIGGER skincare_tip_BEFORE_DELETE
BEFORE DELETE ON skincare_tip
FOR EACH ROW
BEGIN
INSERT INTO skincare_tip_h (
    tip_id,
    category,
    title,
    content,
    modified_timestamp
) VALUES (
             OLD.tip_id,
             OLD.category,
             OLD.title,
             OLD.content,
             NOW()
         );
END$$

DELIMITER ;