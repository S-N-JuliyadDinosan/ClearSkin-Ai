-- Drop triggers if exist
DROP TRIGGER IF EXISTS appointment_before_insert;
DROP TRIGGER IF EXISTS appointment_before_update;
DROP TRIGGER IF EXISTS appointment_before_delete;

-- Create history table
CREATE TABLE IF NOT EXISTS appointment_history (
                                                   history_id VARCHAR(255) PRIMARY KEY,
    appointment_id BIGINT,
    user_id BIGINT,
    user_email VARCHAR(255),
    user_name VARCHAR(100),
    date DATETIME,
    doctor_id BIGINT,
    status VARCHAR(20),
    action_type VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Change delimiter
DELIMITER $$

-- Insert trigger
CREATE TRIGGER appointment_before_insert
    BEFORE INSERT ON appointment
    FOR EACH ROW
BEGIN
    INSERT INTO appointment_history (
        history_id, appointment_id, user_id, user_email, user_name, date, doctor_id, status, action_type, timestamp
    ) VALUES (
                 UUID(), NEW.appointment_id, NEW.user_id, NEW.user_email, NEW.user_name,
                 NEW.date, NEW.doctor_id, NEW.status, 'INSERT', NOW()
             );
    END$$

    -- Update trigger
    CREATE TRIGGER appointment_before_update
        BEFORE UPDATE ON appointment
        FOR EACH ROW
    BEGIN
        INSERT INTO appointment_history (
            history_id, appointment_id, user_id, user_email, user_name, date, doctor_id, status, action_type, timestamp
        ) VALUES (
                     UUID(), NEW.appointment_id, NEW.user_id, NEW.user_email, NEW.user_name,
                     NEW.date, NEW.doctor_id, NEW.status, 'UPDATE', NOW()
                 );
        END$$

        -- Delete trigger
        CREATE TRIGGER appointment_before_delete
            BEFORE DELETE ON appointment
            FOR EACH ROW
        BEGIN
            INSERT INTO appointment_history (
                history_id, appointment_id, user_id, user_email, user_name, date, doctor_id, status, action_type, timestamp
            ) VALUES (
                         UUID(), OLD.appointment_id, OLD.user_id, OLD.user_email, OLD.user_name,
                         OLD.date, OLD.doctor_id, OLD.status, 'DELETE', NOW()
                     );
            END$$

            DELIMITER ;
