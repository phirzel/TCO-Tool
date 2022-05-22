-- ***************************************************************
-- Description:	NLS-Schema extension (SQL-DLL) supported by JOMM
-- Target: 	MySQL 4.1 (or higher); engine=InnoDB
-- Depends:	JOMM/sql/T_Key_Object_MySQL.sql
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

DROP TABLE IF EXISTS T_MAP_NLS;
DROP TABLE IF EXISTS T_Translation;
DROP TABLE IF EXISTS T_NLS;

-- Technical table: Multi-language Strings
-- see T_Translation for concrete entries
-- see UML <PIM-type> "TextNls"
-- see ch.softenvironment.jomm.datatypes.DbNlsString
CREATE TABLE T_NLS
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    Symbol       VARCHAR(61) NULL,
    T_CreateDate DATETIME    NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
ALTER TABLE T_NLS COMMENT 'Technical table: Multi-language Strings';

-- Technical table: Concrete country/language specific translations
-- see T_NLS
CREATE TABLE T_Translation
(
    T_Id_Nls     INTEGER      NOT NULL,
    Language     CHAR(2)      NOT NULL, -- ENUM('en', 'de', 'fr', 'it', ...)
    Country      CHAR(2) NULL,          -- ENUM('GB', 'CH', 'FR', 'IT', ...)
    NlsText      VARCHAR(256) NOT NULL, -- @DbTextFieldDescriptor.NLS
    T_CreateDate DATETIME     NOT NULL,
    T_LastChange TIMESTAMP    NOT NULL,
    T_User       VARCHAR(30)  NOT NULL,
    CONSTRAINT T_Ix_PK_Translation PRIMARY KEY (Language, T_Id_Nls)
);
CREATE
UNIQUE INDEX T_IX_Translation ON T_Translation (
       T_Id_Nls,
       Language,
       Country                        
);
ALTER TABLE T_Translation
    ADD FOREIGN KEY (T_Id_Nls)
        REFERENCES T_NLS (T_Id)
        ON DELETE CASCADE;
ALTER TABLE T_Translation COMMENT 'Technical table: Multi-language translation';

-- Technical table: Translations for Attributes with multiple type: DbNlsString[0..*]
-- see T_NLS
CREATE TABLE T_MAP_NLS
(
    T_Type_Owner      VARCHAR(30) NOT NULL,
    T_Id_Owner        INTEGER     NOT NULL,
    T_Attribute_Owner VARCHAR(30) NOT NULL,
    T_Id_Value        INTEGER     NOT NULL,
    T_CreateDate      DATETIME    NOT NULL,
    T_LastChange      TIMESTAMP   NOT NULL,
    T_User            VARCHAR(30) NOT NULL,
    CONSTRAINT T_Ix_PK_MAP_NLS PRIMARY KEY (T_Type_Owner, T_Id_Owner, T_Attribute_Owner, T_Id_Value)
);
ALTER TABLE T_MAP_NLS
    ADD FOREIGN KEY (T_Id_Value) REFERENCES T_NLS (T_Id);
ALTER TABLE T_MAP_NLS COMMENT 'Technical table: T_NLS[0..*]';


-- Key/Sequence Handling
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_LastChange, T_User)
VALUES ('T_NLS', 100000, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');