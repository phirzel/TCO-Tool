-- ***************************************************************
-- Description:	NLS-Schema extension supported by JOMM
-- Target: 	ORACLE 9i (or higher)
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- DROP TABLE T_MAP_NLS;
-- DROP TABLE T_Translation;
-- DROP TABLE T_NLS;
-- DROP SEQUENCE T_Key_T_NLS;


-- SQL DDL
CREATE TABLE T_NLS
(
    T_Id         NUMBER(18) NOT NULL PRIMARY KEY,
    T_User       VARCHAR2(30) NOT NULL,
    T_LastChange DATE NOT NULL,
    Symbol       VARCHAR2(61) NULL,
    T_CreateDate DATE NOT NULL
);
-- ALTER TABLE T_NLS ADD  (PRIMARY KEY (T_Id));

CREATE TABLE T_Translation
(
    Language     CHAR(2) NOT NULL,
    T_Id_Nls     NUMBER(18) NOT NULL,
    Country      CHAR(2) NULL,
    NlsText      VARCHAR2(1024) NOT NULL,
    T_User       VARCHAR2(30) NOT NULL,
    T_LastChange DATE    NOT NULL,
    T_CreateDate DATE    NOT NULL
);
ALTER TABLE T_Translation
    ADD (PRIMARY KEY (Language, T_Id_Nls));
ALTER TABLE T_Translation
    ADD (FOREIGN KEY (T_Id_Nls) REFERENCES T_NLS ON DELETE CASCADE);
CREATE
UNIQUE INDEX T_Ix_T_Translation ON T_Translation (
       Language,
       T_Id_Nls,
       Country                        
);

CREATE TABLE T_MAP_NLS
(
    T_Type_Owner      VARCHAR2(30) NOT NULL,
    T_Id_Owner        NUMBER(18) NOT NULL,
    T_Attribute_Owner VARCHAR2(30) NOT NULL,
    T_Id_Value        NUMBER(18) NOT NULL,
    T_CreateDate      DATE NOT NULL,
    T_LastChange      DATE NOT NULL,
    T_User            VARCHAR2(30) NOT NULL
);
ALTER TABLE T_MAP_NLS
    ADD (PRIMARY KEY (T_Type_Owner, T_Id_Owner, T_Attribute_Owner, T_Id_Value));
ALTER TABLE T_MAP_NLS
    ADD (FOREIGN KEY (T_Id_Value) REFERENCES T_NLS);


-- Key/Sequence Handling
CREATE SEQUENCE T_Key_T_NLS INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCYCLE NOCACHE NOORDER;
