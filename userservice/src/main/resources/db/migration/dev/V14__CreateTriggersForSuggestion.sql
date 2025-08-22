DROP TRIGGER IF EXISTS user_service_db_clearskin.suggestion_BEFORE_INSERT;
DROP TRIGGER IF EXISTS user_service_db_clearskin.suggestion_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS user_service_db_clearskin.suggestion_BEFORE_DELETE;

DELIMITER $$

USE user_service_db_clearskin$$

-- Create UPDATE trigger
CREATE DEFINER = CURRENT_USER TRIGGER suggestion_BEFORE_UPDATE
BEFORE UPDATE ON suggestion
                            FOR EACH ROW
BEGIN
INSERT INTO suggestion_h (
    suggestion_id,
    severity,
    text,
    modified_timestamp
) VALUES (
             OLD.suggestion_id,
             OLD.severity,
             OLD.text,
             NOW()
         );
END$$

-- Create DELETE trigger
CREATE DEFINER = CURRENT_USER TRIGGER suggestion_BEFORE_DELETE
BEFORE DELETE ON suggestion
FOR EACH ROW
BEGIN
INSERT INTO suggestion_h (
    suggestion_id,
    severity,
    text,
    modified_timestamp
) VALUES (
             OLD.suggestion_id,
             OLD.severity,
             OLD.text,
             NOW()
         );
END$$

DELIMITER ;