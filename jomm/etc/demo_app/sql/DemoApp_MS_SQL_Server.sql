-- Description:	DemoApp Model for testing and demo reasons of JOMM
-- Target: 	MS SQL Server 2005 (or higher)
-- Depends:	JOMM/sql/T_Key_Object_MS_SQL_Server.sql
--		JOMM/sql/NLS_Schema_MS_SQL_Server.sql
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- SQL DDL
CREATE /*[MySchemaName].*/ TABLE Project
(
    T_Id         BIGINT       NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30)  NOT NULL,
    Name         VARCHAR(255) NOT NULL,
    Active       CHAR(1),  -- Pseudo Boolean
    Start        DATETIME, -- DATE
    T_CreateDate DATETIME     NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME     NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30)  NOT NULL DEFAULT system_user
);
-- abstract Person
CREATE TABLE Person
(
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30) NOT NULL,
    Name         VARCHAR(255),
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE NaturalPerson
(                          -- extends Person
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    FirstName    VARCHAR(100),
    Birthday     DATETIME, -- DATE
    sex          CHAR(1),
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE LegalPerson
(                              -- extends Person
    T_Id             BIGINT      NOT NULL PRIMARY KEY,
    T_Id_CompanyType BIGINT      NOT NULL,
    Formation        DATETIME, -- DATE
    T_CreateDate     DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange     DATETIME    NOT NULL DEFAULT getDate(),
    T_User           VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE Role
( -- n:n MAP between Person and Project
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30) NOT NULL,
    T_Id_Member  BIGINT      NOT NULL,
    T_Id_Project BIGINT      NOT NULL,
    T_Id_Type    BIGINT      NOT NULL,
    Percentage   FLOAT,
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE Activity
(
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30) NOT NULL,
    T_Id_Project BIGINT      NOT NULL,
    T_Id_Phase   BIGINT      NOT NULL,
    T_Id_Role    BIGINT      NOT NULL,
    Description  VARCHAR(1024),
    Effort       FLOAT,
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE WorkProduct
(
    T_Id              BIGINT      NOT NULL PRIMARY KEY,
    T_Type            VARCHAR(30) NOT NULL,
    T_Id_Responsible  BIGINT,
    T_Id_name         BIGINT      NOT NULL, -- [1:1]
    T_Id_optionalName BIGINT,               -- [1:0..1]
-- T_Id_MultiName BIGINT,  [0..*]
    Description       VARCHAR(20),
    T_CreateDate      DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange      DATETIME    NOT NULL DEFAULT getDate(),
    T_User            VARCHAR(30) NOT NULL DEFAULT system_user
);
-- DbNlsString
ALTER TABLE WorkProduct
    ADD FOREIGN KEY (T_Id_name) REFERENCES T_NLS (T_Id)
        ON DELETE CASCADE;
ALTER TABLE WorkProduct
    ADD FOREIGN KEY (T_Id_optionalName) REFERENCES T_NLS (T_Id)
        ON DELETE CASCADE;


CREATE TABLE CompanyType
(
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    T_Id_Name    BIGINT      NOT NULL,
    IliCode      VARCHAR(254),
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE Phase
(
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    T_Id_Name    BIGINT      NOT NULL,
    T_Sequence   BIGINT      NOT NULL,
    IliCode      VARCHAR(254),
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);
CREATE TABLE RoleType
(
    T_Id         BIGINT      NOT NULL PRIMARY KEY,
    T_Id_Name    BIGINT      NOT NULL,
    IliCode      VARCHAR(254),
    T_CreateDate DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange DATETIME    NOT NULL DEFAULT getDate(),
    T_User       VARCHAR(30) NOT NULL DEFAULT system_user
);

-- DbEnumeration Index
CREATE
UNIQUE INDEX T_Ix_CompanyType_IliCode ON CompanyType(IliCode);
CREATE
UNIQUE INDEX T_Ix_Phase_IliCode ON Phase(IliCode);
CREATE
UNIQUE INDEX T_Ix_RoleType_IliCode ON RoleType(IliCode);

-- Inheritance
ALTER TABLE NaturalPerson
    ADD FOREIGN KEY (T_Id) REFERENCES Person (T_Id)
        ON DELETE CASCADE;
ALTER TABLE LegalPerson
    ADD FOREIGN KEY (T_Id) REFERENCES Person (T_Id)
        ON DELETE CASCADE;

-- 1:n
ALTER TABLE Activity
    ADD FOREIGN KEY (T_Id_Project) REFERENCES Project (T_Id)
        ON DELETE CASCADE;
ALTER TABLE Activity
    ADD FOREIGN KEY (T_Id_Role) REFERENCES Role (T_Id)
        ON DELETE CASCADE;
ALTER TABLE WorkProduct
    ADD FOREIGN KEY (T_Id_Responsible) REFERENCES NaturalPerson (T_Id);

-- Codes
ALTER TABLE LegalPerson
    ADD FOREIGN KEY (T_Id_CompanyType) REFERENCES CompanyType (T_Id)
        ON DELETE NO ACTION; --RESTRICT
ALTER TABLE Activity
    ADD FOREIGN KEY (T_Id_Phase) REFERENCES Phase (T_Id)
        ON DELETE NO ACTION; --RESTRICT
ALTER TABLE Role
    ADD FOREIGN KEY (T_Id_Type) REFERENCES RoleType (T_Id)
        ON DELETE NO ACTION;
--RESTRICT
-- n:n Map
ALTER TABLE Role
    ADD FOREIGN KEY (T_Id_Member) REFERENCES Person (T_Id)
        ON DELETE CASCADE;
ALTER TABLE Role
    ADD FOREIGN KEY (T_Id_Project) REFERENCES Project (T_Id)
        ON DELETE NO ACTION; -- TODO CASCADE => cyclic or multiple cascade path???
CREATE
UNIQUE INDEX T_Ix_UniqueRole ON Role (
     T_Id_Member,
     T_Id_Project,
     T_Id_Type
);

-- Key/Sequence Handling
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_User)
VALUES ('Activity', 1, '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_User)
VALUES ('Person', 1, '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_User)
VALUES ('Project', 1, '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_User)
VALUES ('WorkProduct', 1, '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_User)
VALUES ('Role', 1, '2005-03-14 20:00:00', 'phirzel');