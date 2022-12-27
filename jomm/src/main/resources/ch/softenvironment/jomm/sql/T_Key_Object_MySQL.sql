-- ***************************************************************
-- Description:	Table to manage Keys for any application Object
--		Supports an "artifical Sequence-mechanism" by JOMM
-- Target: 	MySQL 4.1 (or higher)
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************
-- Description:	Table to manage Keys for any application Object
--		Supports an "artifical Sequence-mechanism" by JOMM
-- Target: 	MySQL 4.1 (or higher); engine=InnoDB
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

DROP TABLE IF EXISTS T_Key_Object;

CREATE TABLE T_Key_Object
(
    T_Key          VARCHAR(30) NOT NULL,
    T_LastUniqueId INTEGER     NOT NULL,
    T_CreateDate   DATETIME    NOT NULL,
    T_LastChange   TIMESTAMP   NOT NULL,
    T_User         VARCHAR(30) NOT NULL,
    CONSTRAINT PK PRIMARY KEY (T_Key)
);
ALTER TABLE T_Key_Object COMMENT 'Technical table: Sequence';