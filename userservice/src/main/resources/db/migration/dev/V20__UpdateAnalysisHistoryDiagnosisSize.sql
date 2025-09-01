-- 1. Update column in analysis_history to TEXT
ALTER TABLE analysis_history
    MODIFY COLUMN diagnosis TEXT;

-- 2. Update column in analysis_history_h to TEXT
ALTER TABLE analysis_history_h
    MODIFY COLUMN diagnosis TEXT;

-- 3. Drop old triggers
DROP TRIGGER IF EXISTS analysis_history_BEFORE_UPDATE;
DROP TRIGGER IF EXISTS analysis_history_BEFORE_DELETE;

DELIMITER $$

-- 4. Recreate UPDATE trigger with diagnosis
CREATE TRIGGER analysis_history_BEFORE_UPDATE
    BEFORE UPDATE ON analysis_history
    FOR EACH ROW
BEGIN
    INSERT INTO analysis_history_h (
        user_id,
        severity,
        confidence,
        diagnosis,
        analysis_time,
        modified_timestamp
    ) VALUES (
                 OLD.user_id,
                 OLD.severity,
                 OLD.confidence,
                 OLD.diagnosis,
                 OLD.analysis_time,
                 NOW()
             );
    END$$

    -- 5. Recreate DELETE trigger with diagnosis
    CREATE TRIGGER analysis_history_BEFORE_DELETE
        BEFORE DELETE ON analysis_history
        FOR EACH ROW
    BEGIN
        INSERT INTO analysis_history_h (
            user_id,
            severity,
            confidence,
            diagnosis,
            analysis_time,
            modified_timestamp
        ) VALUES (
                     OLD.user_id,
                     OLD.severity,
                     OLD.confidence,
                     OLD.diagnosis,
                     OLD.analysis_time,
                     NOW()
                 );
        END$$

        DELIMITER ;
