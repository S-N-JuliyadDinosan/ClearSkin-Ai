ALTER TABLE users
    ADD COLUMN role VARCHAR(50) AFTER name;

ALTER TABLE users_h
    ADD COLUMN role VARCHAR(50) AFTER name;

DROP TRIGGER IF EXISTS users_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS users_BEFORE_DELETE;

DELIMITER $$

CREATE TRIGGER users_BEFORE_UPDATE
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    INSERT INTO users_h (
        user_id,
        email,
        password,
        name,
        role,
        created_at,
        modified_timestamp
    ) VALUES (
                 OLD.user_id,
                 OLD.email,
                 OLD.password,
                 OLD.name,
                 OLD.role,
                 OLD.created_at,
                 NOW()
             );
    END$$

    CREATE TRIGGER users_BEFORE_DELETE
        BEFORE DELETE ON users
        FOR EACH ROW
    BEGIN
        INSERT INTO users_h (
            user_id,
            email,
            password,
            name,
            role,
            created_at,
            modified_timestamp
        ) VALUES (
                     OLD.user_id,
                     OLD.email,
                     OLD.password,
                     OLD.name,
                     OLD.role,
                     OLD.created_at,
                     NOW()
                 );
        END$$

        DELIMITER ;
