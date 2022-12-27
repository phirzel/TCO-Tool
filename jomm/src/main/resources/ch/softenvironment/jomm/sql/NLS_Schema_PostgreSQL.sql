-- ***************************************************************
-- Description:	NLS-Schema extension supported by JOMM
-- Target: 	PostgreSQL 8.0 (or higher)
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- SQL DDL
CREATE TABLE T_MAP_NLS
(
    T_Type_Owner      VARCHAR(30) NOT NULL,
    T_Id_Owner        INTEGER     NOT NULL,
    T_Attribute_Owner VARCHAR(30) NOT NULL,
    T_Id_Value        INTEGER     NOT NULL,
    T_CreateDate      TIMESTAMP   NOT NULL,
    T_LastChange      TIMESTAMP   NOT NULL,
    T_User            VARCHAR(30) NOT NULL,
    CONSTRAINT T_Ix_PK_MAP_NLS PRIMARY KEY (T_Type_Owner, T_Id_Owner, T_Attribute_Owner, T_Id_Value)
);
CREATE TABLE T_NLS
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    Symbol       VARCHAR(61) NULL,
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE T_Translation
(
    T_Id_Nls     INTEGER      NOT NULL,
    Language     CHAR(2)      NOT NULL,
    Country      CHAR(2) NULL,
    NlsText      VARCHAR(256) NOT NULL, -- @DbTextFieldDescriptor.NLS
    T_CreateDate TIMESTAMP    NOT NULL,
    T_LastChange TIMESTAMP    NOT NULL,
    T_User       VARCHAR(30)  NOT NULL,
    CONSTRAINT T_Ix_PK_Translation PRIMARY KEY (Language, T_Id_Nls)
);

CREATE
UNIQUE INDEX T_IX_UniqueEntry ON T_Translation (
       T_Id_Nls,
       Language,
       Country                        
);

ALTER TABLE T_MAP_NLS
    ADD FOREIGN KEY (T_Id_Value) REFERENCES T_NLS (T_Id);

ALTER TABLE T_Translation
    ADD FOREIGN KEY (T_Id_Nls)
        REFERENCES T_NLS (T_Id)
        ON DELETE CASCADE;

-- Key/Sequence handling
CREATE SEQUENCE T_Key_T_NLS INCREMENT BY 1 START WITH 1 MINVALUE 1 NO CYCLE;