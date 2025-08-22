DROP TRIGGER IF EXISTS user_service_db_clearskin.analysis_history_BEFORE_INSERT;
DROP TRIGGER IF EXISTS user_service_db_clearskin.analysis_history_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS user_service_db_clearskin.analysis_history_BEFORE_DELETE;

DELIMITER $$

USE user_service_db_clearskin$$

-- Create UPDATE trigger
CREATE DEFINER = CURRENT_USER TRIGGER analysis_history_BEFORE_UPDATE
BEFORE UPDATE ON analysis_history
                            FOR EACH ROW
BEGIN
INSERT INTO analysis_history_h (
    user_id,
    severity,
    analysis_time,
    modified_timestamp
) VALUES (
             OLD.user_id,
             OLD.severity,
             OLD.analysis_time,
             NOW()
         );
END$$

-- Create DELETE trigger
CREATE DEFINER = CURRENT_USER TRIGGER analysis_history_BEFORE_DELETE
BEFORE DELETE ON analysis_history
FOR EACH ROW
BEGIN
INSERT INTO analysis_history_h (
    user_id,
    severity,
    analysis_time,
    modified_timestamp
) VALUES (
             OLD.user_id,
             OLD.severity,
             OLD.analysis_time,
             NOW()
         );
END$$

DELIMITER ;