-- ***************************************************************
-- Description:	Test data for DemoApp Model (for testing and demo reasons of JOMM)
-- Target: 	MySQL 4.1 (or higher)
-- Depends:	JOMM/sql/T_Key_Object_MySQL.sql
--		JOMM/sql/NLS_Schema_MySQL.sql
--		JOMM/sql/DemoApp_*.sql
-- ***************************************************************
-- by:		Peter Hirzel, http://www.softEnvironment.ch
-- ***************************************************************

-- add Enumerations
-- CompanyType
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (100, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (100, 'de', 'AG', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO CompanyType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (1, 100, 'AG', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (101, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (101, 'de', 'GmbH', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO CompanyType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (2, 101, 'GmbH', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (102, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (102, 'de', 'Verein', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO CompanyType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (3, 102, 'Verein', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (103, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (103, 'de', 'Einzelfirma', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO CompanyType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (4, 103, 'Einzelfirma', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');


-- Phase
INSERT INTO T_NLS (T_Id, Symbol, T_CreateDate, T_LastChange, T_User)
VALUES (200, 'Phase->Inception', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (200, 'en', 'Inception', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO Phase (T_Id, T_Id_Name, T_Sequence, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (2, 200, 1, 'Inception', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, Symbol, T_CreateDate, T_LastChange, T_User)
VALUES (201, 'Phase->Elaboration', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (201, 'en', 'Elaboration', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO Phase (T_Id, T_Id_Name, T_Sequence, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (1, 201, 2, 'Elaboration', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (202, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (202, 'en', 'Construction', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO Phase (T_Id, T_Id_Name, T_Sequence, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (3, 202, 3, 'Construction', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (203, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (203, 'en', 'Transition', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO Phase (T_Id, T_Id_Name, T_Sequence, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (4, 203, 4, 'Transition', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');

-- RoleType
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (300, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (300, 'de', 'Qualitaets-Verantwortlicher', '2005-03-14 20:00:00', '2005-03-14 20:00:00',
        'phirzel');
INSERT INTO RoleType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (1, 300, 'QualityResponsible', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (301, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (301, 'de', 'Projektleiter', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO RoleType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (2, 301, 'ProjectManager', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (302, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (302, 'de', 'Entwickler', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO RoleType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (3, 302, 'Developer', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_NLS (T_Id, T_CreateDate, T_LastChange, T_User)
VALUES (303, '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO T_Translation (T_Id_Nls, Language, NlsText, T_CreateDate, T_LastChange, T_User)
VALUES (303, 'de', 'Tester', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');
INSERT INTO RoleType (T_Id, T_Id_Name, IliCode, T_CreateDate, T_LastChange, T_User)
VALUES (4, 303, 'Tester', '2005-03-14 20:00:00', '2005-03-14 20:00:00', 'phirzel');