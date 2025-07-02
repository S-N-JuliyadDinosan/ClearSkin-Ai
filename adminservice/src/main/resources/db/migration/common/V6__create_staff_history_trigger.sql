-- Drop triggers if exist
DROP TRIGGER IF EXISTS staff_before_insert;
DROP TRIGGER IF EXISTS staff_before_update;
DROP TRIGGER IF EXISTS staff_before_delete;

-- Create history table
CREATE TABLE IF NOT EXISTS staff_history (
                                             history_id VARCHAR(255) PRIMARY KEY,
    staff_id BIGINT,
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(20),
    action_type VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

DELIMITER $$

-- Insert trigger
CREATE TRIGGER staff_before_insert
    BEFORE INSERT ON staff
    FOR EACH ROW
BEGIN
    INSERT INTO staff_history (
        history_id, staff_id, email, password, role, action_type, timestamp
    ) VALUES (
                 UUID(), NEW.staff_id, NEW.email, NEW.password, NEW.role, 'INSERT', NOW()
             );
    END$$

    -- Update trigger
    CREATE TRIGGER staff_before_update
        BEFORE UPDATE ON staff
        FOR EACH ROW
    BEGIN
        INSERT INTO staff_history (
            history_id, staff_id, email, password, role, action_type, timestamp
        ) VALUES (
                     UUID(), NEW.staff_id, NEW.email, NEW.password, NEW.role, 'UPDATE', NOW()
                 );
        END$$

        -- Delete trigger
        CREATE TRIGGER staff_before_delete
            BEFORE DELETE ON staff
            FOR EACH ROW
        BEGIN
            INSERT INTO staff_history (
                history_id, staff_id, email, password, role, action_type, timestamp
            ) VALUES (
                         UUID(), OLD.staff_id, OLD.email, OLD.password, OLD.role, 'DELETE', NOW()
                     );
            END$$

            DELIMITER ;
