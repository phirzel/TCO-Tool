-- ***************************************************************
-- Description:	DemoApp Model for testing and demo reasons of JOMM
-- Target: 	MS Access 2002 (or higher)
-- Depends:	JOMM/sql/T_Key_Object_MS_Access.sql
--		JOMM/sql/NLS_Schema_MS_Access.sql
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- SQL DDL
CREATE TABLE Project
(
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Type       Text(30) NOT NULL,
    Name         Text(255) NOT NULL,
    Active       Text(1), -- Pseudo Boolean
    Start        DATE,
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
-- abstract Person
CREATE TABLE Person
(
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Type       Text(30) NOT NULL,
    Name         Text(255),
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
CREATE TABLE NaturalPerson
( -- extends Person
    T_Id         LONG      NOT NULL PRIMARY KEY,
    FirstName    Text(100),
    Birthday     DATE,
    sex          Text(1),
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
CREATE TABLE LegalPerson
( -- extends Person
    T_Id             LONG      NOT NULL PRIMARY KEY,
    T_Id_CompanyType LONG      NOT NULL,
    Formation        DATE,
    T_CreateDate     DATETIME  NOT NULL,
    T_LastChange     TIMESTAMP NOT NULL,
    T_User           Text(30) NOT NULL
);
CREATE TABLE Role
( -- n:n MAP between Person and Project
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Type       Text(30) NOT NULL,
    T_Id_Member  LONG      NOT NULL,
    T_Id_Project LONG      NOT NULL,
    T_Id_Type    LONG      NOT NULL,
    Percentage   DOUBLE,
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
CREATE TABLE Activity
(
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Type       Text(30) NOT NULL,
    T_Id_Project LONG      NOT NULL,
    T_Id_Phase   LONG      NOT NULL,
    T_Id_Role    LONG      NOT NULL,
    Description  Memo,
    Effort       Double,
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
CREATE TABLE WorkProduct
(
    T_Id              LONG      NOT NULL PRIMARY KEY,
    T_Type            Text(30) NOT NULL,
    T_Id_responsible  LONG,
    T_Id_name         LONG      NOT NULL, -- [1:1]
    T_Id_optionalName LONG,               -- [1:0..1]
-- T_Id_MultiName LONG,  [0..*]
    Description       Text(20),
    T_CreateDate      DATETIME  NOT NULL,
    T_LastChange      TIMESTAMP NOT NULL,
    T_User            Text(30) NOT NULL
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
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Id_Name    LONG      NOT NULL,
    IliCode      Text(254),
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
CREATE TABLE Phase
(
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Id_Name    LONG      NOT NULL,
    T_Sequence   LONG      NOT NULL,
    IliCode      Text(254) NOT NULL,
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
);
CREATE TABLE RoleType
(
    T_Id         LONG      NOT NULL PRIMARY KEY,
    T_Id_Name    LONG      NOT NULL,
    IliCode      Text(254) NOT NULL,
    T_CreateDate DATETIME  NOT NULL,
    T_LastChange TIMESTAMP NOT NULL,
    T_User       Text(30) NOT NULL
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

-- TODO Ref Phase.T_Id_Name<#>--T_NLS.T_Id
-- TODO Unique Index Phase.IliCode

-- Key/Sequence Handling
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_LastChange, T_User)
VALUES ('Activity', 1, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_LastChange, T_User)
VALUES ('Person', 1, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_LastChange, T_User)
VALUES ('Project', 1, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_LastChange, T_User)
VALUES ('WorkProduct', 1, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Key_Object (T_Key, T_LastUniqueId, T_CreateDate, T_LastChange, T_User)
VALUES ('Role', 1, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');


