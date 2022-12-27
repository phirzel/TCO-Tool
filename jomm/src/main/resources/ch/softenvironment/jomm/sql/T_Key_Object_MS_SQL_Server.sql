-- ***************************************************************
-- Description:	Table to manage Keys for any application Object
--		Supports an "artifical Sequence-mechanism" by JOMM
-- Target: 	MS SQL Server 2005 (or higher)
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- SQL DDL
CREATE TABLE T_Key_Object
(
    T_Key          VARCHAR(30) NOT NULL PRIMARY KEY,
    T_LastUniqueId BIGINT      NOT NULL,
    T_CreateDate   DATETIME    NOT NULL DEFAULT getDate(),
    T_LastChange   DATETIME    NOT NULL DEFAULT getDate(),
    T_User         VARCHAR(30) NOT NULL DEFAULT system_user
    -- CONSTRAINT PK_T_Key_Object PRIMARY KEY (T_Key)
);
