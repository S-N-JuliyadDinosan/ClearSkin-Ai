-- Drop triggers if exist
DROP TRIGGER IF EXISTS doctor_before_insert;
DROP TRIGGER IF EXISTS doctor_before_update;
DROP TRIGGER IF EXISTS doctor_before_delete;

-- Create history table
CREATE TABLE IF NOT EXISTS doctor_history (
                                              history_id VARCHAR(255) PRIMARY KEY,
    doctor_id BIGINT,
    name VARCHAR(100),
    qualifications TEXT,
    speciality VARCHAR(50),
    action_type VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

DELIMITER $$

-- Insert trigger
CREATE TRIGGER doctor_before_insert
    BEFORE INSERT ON doctor
    FOR EACH ROW
BEGIN
    INSERT INTO doctor_history (
        history_id, doctor_id, name, qualifications, speciality, action_type, timestamp
    ) VALUES (
                 UUID(), NEW.doctor_id, NEW.name, NEW.qualifications, NEW.speciality, 'INSERT', NOW()
             );
    END$$

    -- Update trigger
    CREATE TRIGGER doctor_before_update
        BEFORE UPDATE ON doctor
        FOR EACH ROW
    BEGIN
        INSERT INTO doctor_history (
            history_id, doctor_id, name, qualifications, speciality, action_type, timestamp
        ) VALUES (
                     UUID(), NEW.doctor_id, NEW.name, NEW.qualifications, NEW.speciality, 'UPDATE', NOW()
                 );
        END$$

        -- Delete trigger
        CREATE TRIGGER doctor_before_delete
            BEFORE DELETE ON doctor
            FOR EACH ROW
        BEGIN
            INSERT INTO doctor_history (
                history_id, doctor_id, name, qualifications, speciality, action_type, timestamp
            ) VALUES (
                         UUID(), OLD.doctor_id, OLD.name, OLD.qualifications, OLD.speciality, 'DELETE', NOW()
                     );
            END$$

            DELIMITER ;
