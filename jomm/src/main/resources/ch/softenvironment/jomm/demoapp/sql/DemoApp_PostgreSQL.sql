-- ***************************************************************
-- Description:	DemoApp Model for testing and demo reasons of JOMM
-- Target: 	PostgreSQL 8.0 (or higher)
-- Depends:	JOMM/sql/NLS_Schema_PostgreSQL.sql
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- DDL
CREATE TABLE Project
(
    T_Id         INTEGER      NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30)  NOT NULL,
    Name         VARCHAR(255) NOT NULL,
    Active       CHAR(1), -- Pseudo Boolean
    Start        DATE,
    T_CreateDate TIMESTAMP    NOT NULL,
    T_LastChange TIMESTAMP    NOT NULL,
    T_User       VARCHAR(30)  NOT NULL
);
-- abstract Person
CREATE TABLE Person
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30) NOT NULL,
    Name         VARCHAR(255),
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE NaturalPerson
( -- extends Person
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    FirstName    VARCHAR(100),
    Birthday     DATE,
    sex          CHAR(1),
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE LegalPerson
( -- extends Person
    T_Id             INTEGER     NOT NULL PRIMARY KEY,
    T_Id_CompanyType INTEGER     NOT NULL,
    Formation        DATE,
    T_CreateDate     TIMESTAMP   NOT NULL,
    T_LastChange     TIMESTAMP   NOT NULL,
    T_User           VARCHAR(30) NOT NULL
);
CREATE TABLE Role
( -- n:n MAP between Person and Project
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30) NOT NULL,
    T_Id_Member  INTEGER     NOT NULL,
    T_Id_Project INTEGER     NOT NULL,
    T_Id_Type    INTEGER     NOT NULL,
    Percentage   FLOAT4,
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE Activity
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    T_Type       VARCHAR(30) NOT NULL,
    T_Id_Project INTEGER     NOT NULL,
    T_Id_Phase   INTEGER     NOT NULL,
    T_Id_Role    INTEGER     NOT NULL,
    Description  VARCHAR(1024),
    Effort       FLOAT8,
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE WorkProduct
(
    T_Id              INTEGER     NOT NULL PRIMARY KEY,
    T_Type            VARCHAR(30) NOT NULL,
    T_Id_Responsible  INTEGER,
    T_Id_name         INTEGER     NOT NULL, -- [1:1]
    T_Id_optionalName INTEGER,              -- [1:0..1]
-- T_Id_MultiName INTEGER,  [0..*]
    Description       VARCHAR(20),
    T_CreateDate      TIMESTAMP   NOT NULL,
    T_LastChange      TIMESTAMP   NOT NULL,
    T_User            VARCHAR(30) NOT NULL
);
-- 1:1 DbNlsString
-- CREATE INDEX T_Ix_WorkProductName ON WorkProduct (T_Id_Name);
ALTER TABLE WorkProduct
    ADD FOREIGN KEY (T_Id_Name) REFERENCES T_NLS (T_Id)
        ON DELETE CASCADE;
ALTER TABLE WorkProduct
    ADD FOREIGN KEY (T_Id_optionalName) REFERENCES T_NLS (T_Id)
        ON DELETE CASCADE;

CREATE TABLE CompanyType
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    T_Id_Name    INTEGER     NOT NULL,
    IliCode      VARCHAR(254),
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE Phase
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    T_Id_Name    INTEGER     NOT NULL,
    T_Sequence   INTEGER     NOT NULL,
    IliCode      VARCHAR(254),
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
);
CREATE TABLE RoleType
(
    T_Id         INTEGER     NOT NULL PRIMARY KEY,
    T_Id_Name    INTEGER     NOT NULL,
    IliCode      VARCHAR(254),
    T_CreateDate TIMESTAMP   NOT NULL,
    T_LastChange TIMESTAMP   NOT NULL,
    T_User       VARCHAR(30) NOT NULL
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
-- CREATE INDEX T_Ix_Project ON Activity (T_Id_Project);
ALTER TABLE Activity
    ADD FOREIGN KEY (T_Id_Project) REFERENCES Project (T_Id)
        ON DELETE CASCADE;
-- CREATE INDEX T_Ix_Role ON Activity (T_Id_Role);
ALTER TABLE Activity
    ADD FOREIGN KEY (T_Id_Role) REFERENCES Role (T_Id)
        ON DELETE CASCADE;
-- CREATE INDEX T_Ix_Responsible ON WorkProduct (T_Id_Responsible);
ALTER TABLE WorkProduct
    ADD FOREIGN KEY (T_Id_Responsible) REFERENCES NaturalPerson (T_Id);

-- Codes
-- CREATE INDEX T_Ix_Company ON LegalPerson (T_Id_CompanyType);
ALTER TABLE LegalPerson
    ADD FOREIGN KEY (T_Id_CompanyType) REFERENCES CompanyType (T_Id)
        ON DELETE RESTRICT;
-- CREATE INDEX T_Ix_Phase ON Activity (T_Id_Phase);
ALTER TABLE Activity
    ADD FOREIGN KEY (T_Id_Phase) REFERENCES Phase (T_Id)
        ON DELETE RESTRICT;
-- CREATE INDEX T_Ix_Type ON Role (T_Id_Type);
ALTER TABLE Role
    ADD FOREIGN KEY (T_Id_Type) REFERENCES RoleType (T_Id)
        ON DELETE RESTRICT;
-- n:n Map
-- CREATE INDEX T_Ix_RolePerson ON Role (T_Id_Member);
-- CREATE INDEX T_Ix_RoleProject ON Role (T_Id_Project);
ALTER TABLE Role
    ADD FOREIGN KEY (T_Id_Member) REFERENCES Person (T_Id)
        ON DELETE CASCADE;
ALTER TABLE Role
    ADD FOREIGN KEY (T_Id_Project) REFERENCES Project (T_Id)
        ON DELETE CASCADE;
CREATE
UNIQUE INDEX T_Ix_UniqueRole ON Role (
     T_Id_Member,
     T_Id_Project,
     T_Id_Type
);

-- Key/Sequence Handling
CREATE SEQUENCE T_Key_Activity INCREMENT BY 1 START WITH 1 MINVALUE 1 NO CYCLE;
CREATE SEQUENCE T_Key_Person INCREMENT BY 1 START WITH 1 MINVALUE 1 NO CYCLE;
CREATE SEQUENCE T_Key_Project INCREMENT BY 1 START WITH 1 MINVALUE 1 NO CYCLE;
CREATE SEQUENCE T_Key_WorkProduct INCREMENT BY 1 START WITH 1 MINVALUE 1 NO CYCLE;
CREATE SEQUENCE T_Key_Role INCREMENT BY 1 START WITH 1 MINVALUE 1 NO CYCLE;