-- ***************************************************************
-- Description:	NLS-Schema extension supported by JOMM
-- Target: 	MS SQL Server 2005 (or higher)
-- Depends:	JOMM/sql/T_Key_Object_MS_SQL_Server.sql
-- ***************************************************************
-- Author:	Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

CREATE TABLE T_NLS
(
    T_Id         BIGINT      NOT NULL PRIMARY KEY IDENTITY,
    Symbol       VARCHAR(61) NULL,
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);

CREATE TABLE T_Translation
(
    T_Id_Nls     BIGINT        NOT NULL,
    Language     CHAR(2)       NOT NULL,
    Country      CHAR(2) NULL,
    NlsText      VARCHAR(1024) NOT NULL, -- TEXT > 8000 Characters
    T_CreateDate DATETIME      NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME      NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30)   NOT NULL DEFAULT system_user,
    CONSTRAINT PK_T_Translation PRIMARY KEY (Language, T_Id_Nls)
);
ALTER TABLE T_Translation
    ADD FOREIGN KEY (T_Id_Nls)
        REFERENCES T_NLS (T_Id) ON DELETE CASCADE;
CREATE
UNIQUE INDEX T_IX_UniqueEntry ON T_Translation (
	T_Id_Nls,
	Language,
    Country                        
);

CREATE TABLE T_MAP_NLS
(
    T_Type_Owner      VARCHAR(30) NOT NULL,
    T_Id_Owner        BIGINT      NOT NULL,
    T_Attribute_Owner VARCHAR(30) NOT NULL,
    T_Id_Value        BIGINT      NOT NULL,
    T_CreateDate      DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange      DATETIME    NOT NULL DEFAULT getDate(),
    T_User            VARCHAR(30) NOT NULL DEFAULT system_user,
    CONSTRAINT PK_T_MAP_NLS PRIMARY KEY (T_Type_Owner, T_Id_Owner, T_Attribute_Owner, T_Id_Value)
);
ALTER TABLE T_MAP_NLS
    ADD FOREIGN KEY (T_Id_VALUE)
        REFERENCES T_NLS (T_Id);

-- Key/Sequence Handling by IDENTITY on Primary Key
