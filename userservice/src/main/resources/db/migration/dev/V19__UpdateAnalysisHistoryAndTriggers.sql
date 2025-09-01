-- 1. Add new column to analysis_history
ALTER TABLE analysis_history
    ADD COLUMN diagnosis VARCHAR(255) DEFAULT NULL;

-- 2. Add new column to analysis_history_h
ALTER TABLE analysis_history_h
    ADD COLUMN diagnosis VARCHAR(255) DEFAULT NULL;

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
