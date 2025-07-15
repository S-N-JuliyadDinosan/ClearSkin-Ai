-- Drop triggers if they exist
DROP TRIGGER IF EXISTS admin_service_db_clearskin.doctor_before_insert;
DROP TRIGGER IF EXISTS admin_service_db_clearskin.doctor_before_update;
DROP TRIGGER IF EXISTS admin_service_db_clearskin.doctor_before_delete;

DELIMITER $$

USE admin_service_db_clearskin$$

-- BEFORE INSERT Trigger
CREATE DEFINER = CURRENT_USER TRIGGER doctor_before_insert
BEFORE INSERT ON doctor
FOR EACH ROW
BEGIN
INSERT INTO doctor_h (
    doctor_id,
    name,
    qualifications,
    speciality,
    is_active,
    modified_timestamp
) VALUES (
             NEW.doctor_id,
             NEW.name,
             NEW.qualifications,
             NEW.speciality,
             1,
             CURRENT_TIMESTAMP
         );
END$$

-- BEFORE UPDATE Trigger
CREATE DEFINER = CURRENT_USER TRIGGER doctor_before_update
BEFORE UPDATE ON doctor
                  FOR EACH ROW
BEGIN
INSERT INTO doctor_h (
    doctor_id,
    name,
    qualifications,
    speciality,
    is_active,
    modified_timestamp
) VALUES (
             OLD.doctor_id,
             OLD.name,
             OLD.qualifications,
             OLD.speciality,
             1,
             CURRENT_TIMESTAMP
         );
END$$

-- BEFORE DELETE Trigger
CREATE DEFINER = CURRENT_USER TRIGGER doctor_before_delete
BEFORE DELETE ON doctor
FOR EACH ROW
BEGIN
INSERT INTO doctor_h (
    doctor_id,
    name,
    qualifications,
    speciality,
    is_active,
    modified_timestamp
) VALUES (
             OLD.doctor_id,
             OLD.name,
             OLD.qualifications,
             OLD.speciality,
             0,
             CURRENT_TIMESTAMP
         );
END$$

DELIMITER ;
