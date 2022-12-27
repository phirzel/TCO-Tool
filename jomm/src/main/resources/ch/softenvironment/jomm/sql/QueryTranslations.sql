-- show code translations for an NLS-Object in a row.
SELECT CODE.T_Id      AS CODE_Id,
       CODE.t_id_name AS NLS_Id,
       (SELECT nlstext
        FROM T_Translation
        WHERE (T_Id_Nls = CODE.t_id_name) AND (Language='de')) AS 'de',
       (SELECT nlstext
        FROM T_Translation
        WHERE (T_Id_Nls = CODE.t_id_name) AND (Language='fr')) AS 'fr',
       (SELECT nlstext
        FROM T_Translation
        WHERE (T_Id_Nls = CODE.t_id_name) AND (Language='it')) AS 'it'
FROM judicalauthority CODE;