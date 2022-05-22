-- ***************************************************************
-- Description:	NLS-Schema extension supported by JOMM
-- Target: 	MS Access 2002 (or higher)
-- Depends:	JOMM/sql/T_Key_Object_MS_Access.sql
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- SQL DDL
CREATE TABLE T_MAP_NLS
(
    T_Type_Owner      TEXT(30) NOT NULL,
    T_Id_Owner        LONG NOT NULL,
    T_Attribute_Owner TEXT(30) NOT NULL,
    T_Id_Value        LONG NOT NULL,
    T_CreateDate      DATE NOT NULL,
    T_LastChange      DATE NOT NULL,
    T_User            TEXT(30) NOT NULL,
    CONSTRAINT PK PRIMARY KEY (T_Type_Owner, T_Id_Owner, T_Attribute_Owner, T_Id_Value)
);

CREATE TABLE T_NLS
(
    T_Id         LONG NOT NULL PRIMARY KEY,
    Symbol       TEXT(61) NULL,
    T_CreateDate DATE NOT NULL,
    T_LastChange DATE NOT NULL,
    T_User       TEXT(30) NOT NULL
);

CREATE TABLE T_Translation
(
    T_Id_Nls     LONG    NOT NULL,
    Language     CHAR(2) NOT NULL,
    Country      CHAR(2) NULL,
    NlsText      MEMO    NOT NULL,
    T_CreateDate DATE    NOT NULL,
    T_LastChange DATE    NOT NULL,
    T_User       TEXT(30) NOT NULL,
    CONSTRAINT PK PRIMARY KEY (Language, T_Id_Nls)
);

CREATE
UNIQUE INDEX T_IX_UniqueEntry ON T_Translation (
	T_Id_Nls,
	Language,
        Country                        
);

ALTER TABLE T_MAP_NLS
    ADD FOREIGN KEY (T_Id_Value)
        REFERENCES T_NLS;

ALTER TABLE T_Translation
    ADD FOREIGN KEY (T_Id_Nls)
        REFERENCES T_NLS;
--                             ON DELETE CASCADE;  => provoques Syntax Error in MS Access => do it manually in "Relationship-Graph"


-- Key/Sequence Handling
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_User)
VALUES ('T_NLS', 100000, '2005-03-14 20:00:00', 'phirzel');
