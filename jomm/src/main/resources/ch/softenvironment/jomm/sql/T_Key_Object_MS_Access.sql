-- ***************************************************************
-- Description:	Table to manage Keys for any application Object
--		Supports an "artifical Sequence-mechanism" by JOMM
-- Target: 	MS Access 2002 (or higher)
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- SQL DDL
CREATE TABLE T_Key_Object
(
    T_Key          Text(30) NOT NULL,
    T_LastUniqueId LONG NOT NULL,
    T_CreateDate   DATE NOT NULL,
    T_LastChange   DATE NOT NULL,
    T_User         TEXT(30) NOT NULL,
    CONSTRAINT PK PRIMARY KEY (T_Key)
);
