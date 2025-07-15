-- Drop triggers if they exist
DROP TRIGGER IF EXISTS admin_service_db_clearskin.staff_before_insert;
DROP TRIGGER IF EXISTS admin_service_db_clearskin.staff_before_update;
DROP TRIGGER IF EXISTS admin_service_db_clearskin.staff_before_delete;

DELIMITER $$

USE admin_service_db_clearskin$$

-- BEFORE INSERT Trigger
CREATE DEFINER = CURRENT_USER TRIGGER staff_before_insert
BEFORE INSERT ON staff
FOR EACH ROW
BEGIN
INSERT INTO staff_h (
    staff_id,
    email,
    password,
    role,
    is_active,
    modified_timestamp
) VALUES (
             NEW.staff_id,
             NEW.email,
             NEW.password,
             NEW.role,
             1,
             CURRENT_TIMESTAMP
         );
END$$

-- BEFORE UPDATE Trigger
CREATE DEFINER = CURRENT_USER TRIGGER staff_before_update
BEFORE UPDATE ON staff
                  FOR EACH ROW
BEGIN
INSERT INTO staff_h (
    staff_id,
    email,
    password,
    role,
    is_active,
    modified_timestamp
) VALUES (
             OLD.staff_id,
             OLD.email,
             OLD.password,
             OLD.role,
             1,
             CURRENT_TIMESTAMP
         );
END$$

-- BEFORE DELETE Trigger
CREATE DEFINER = CURRENT_USER TRIGGER staff_before_delete
BEFORE DELETE ON staff
FOR EACH ROW
BEGIN
INSERT INTO staff_h (
    staff_id,
    email,
    password,
    role,
    is_active,
    modified_timestamp
) VALUES (
             OLD.staff_id,
             OLD.email,
             OLD.password,
             OLD.role,
             0,
             CURRENT_TIMESTAMP
         );
END$$

DELIMITER ;
