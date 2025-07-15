-- Drop triggers if they exist
DROP TRIGGER IF EXISTS admin_service_db_clearskin.appointment_before_insert;
DROP TRIGGER IF EXISTS admin_service_db_clearskin.appointment_before_update;
DROP TRIGGER IF EXISTS admin_service_db_clearskin.appointment_before_delete;

DELIMITER $$

USE admin_service_db_clearskin$$

-- BEFORE INSERT Trigger
CREATE DEFINER = CURRENT_USER TRIGGER appointment_before_insert
BEFORE INSERT ON appointment
FOR EACH ROW
BEGIN
INSERT INTO appointment_h (
    appointment_id,
    user_id,
    user_email,
    user_name,
    date,
    doctor_id,
    status,
    is_active,
    modified_timestamp
) VALUES (
             NEW.appointment_id,
             NEW.user_id,
             NEW.user_email,
             NEW.user_name,
             NEW.date,
             NEW.doctor_id,
             NEW.status,
             1,
             CURRENT_TIMESTAMP
         );
END$$

-- BEFORE UPDATE Trigger
CREATE DEFINER = CURRENT_USER TRIGGER appointment_before_update
BEFORE UPDATE ON appointment
                  FOR EACH ROW
BEGIN
INSERT INTO appointment_h (
    appointment_id,
    user_id,
    user_email,
    user_name,
    date,
    doctor_id,
    status,
    is_active,
    modified_timestamp
) VALUES (
             OLD.appointment_id,
             OLD.user_id,
             OLD.user_email,
             OLD.user_name,
             OLD.date,
             OLD.doctor_id,
             OLD.status,
             1,
             CURRENT_TIMESTAMP
         );
END$$

-- BEFORE DELETE Trigger
CREATE DEFINER = CURRENT_USER TRIGGER appointment_before_delete
BEFORE DELETE ON appointment
FOR EACH ROW
BEGIN
INSERT INTO appointment_h (
    appointment_id,
    user_id,
    user_email,
    user_name,
    date,
    doctor_id,
    status,
    is_active,
    modified_timestamp
) VALUES (
             OLD.appointment_id,
             OLD.user_id,
             OLD.user_email,
             OLD.user_name,
             OLD.date,
             OLD.doctor_id,
             OLD.status,
             0,
             CURRENT_TIMESTAMP
         );
END$$

DELIMITER ;
