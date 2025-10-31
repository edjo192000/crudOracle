
-- Eliminar la tabla si existe (para desarrollo)
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE TASKS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Eliminar la secuencia si existe
BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE TASKS_SEQ';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Crear la tabla TASKS
CREATE TABLE TASKS (
                       task_id         NUMBER          NOT NULL,
                       title           VARCHAR2(200)   NOT NULL,
                       description     VARCHAR2(1000),
                       priority        VARCHAR2(20)    DEFAULT 'MEDIUM' NOT NULL,
                       status          VARCHAR2(20)    DEFAULT 'PENDING' NOT NULL,
                       due_date        DATE,
                       assigned_to     VARCHAR2(100),
                       created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       CONSTRAINT pk_tasks PRIMARY KEY (task_id),
                       CONSTRAINT chk_priority CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
                       CONSTRAINT chk_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

-- Crear secuencia para el ID autoincrementable
CREATE SEQUENCE TASKS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

PROMPT
PROMPT ========================================
PROMPT Tabla TASKS creada exitosamente!
PROMPT Secuencia TASKS_SEQ creada!
PROMPT ========================================
PROMPT