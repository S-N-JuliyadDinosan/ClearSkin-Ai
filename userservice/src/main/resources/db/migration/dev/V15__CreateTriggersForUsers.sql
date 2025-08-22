DROP TRIGGER IF EXISTS user_service_db_clearskin.users_BEFORE_INSERT;
DROP TRIGGER IF EXISTS user_service_db_clearskin.users_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS user_service_db_clearskin.users_BEFORE_DELETE;

DELIMITER $$

USE user_service_db_clearskin$$

-- Create UPDATE trigger
CREATE DEFINER = CURRENT_USER TRIGGER users_BEFORE_UPDATE
BEFORE UPDATE ON users
                            FOR EACH ROW
BEGIN
INSERT INTO users_h (
    user_id,
    email,
    password,
    name,
    created_at,
    modified_timestamp
) VALUES (
             OLD.user_id,
             OLD.email,
             OLD.password,
             OLD.name,
             OLD.created_at,
             NOW()
         );
END$$

-- Create DELETE trigger
CREATE DEFINER = CURRENT_USER TRIGGER users_BEFORE_DELETE
BEFORE DELETE ON users
FOR EACH ROW
BEGIN
INSERT INTO users_h (
    user_id,
    email,
    password,
    name,
    created_at,
    modified_timestamp
) VALUES (
             OLD.user_id,
             OLD.email,
             OLD.password,
             OLD.name,
             OLD.created_at,
             NOW()
         );
END$$

DELIMITER ;