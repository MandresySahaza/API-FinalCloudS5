\c postgres

DROP DATABASE finalcloud;

CREATE DATABASE finalcloud;

\c finalcloud


drop table Favoris;
drop table Annonces;
drop table Utilisateurs;
drop table EtatVoitures;
drop table BoiteVitesses;
drop table Energies;
drop table Modeles;
drop table Marques;
drop table PaysMarques;
drop table Categories;




CREATE TABLE IF NOT EXISTS Categories (
    id_Categorie SERIAL PRIMARY KEY,
    nom VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS PaysMarques (
    id_PaysMarque SERIAL PRIMARY KEY,
    nom VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS Marques (
    id_Marque SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    id_PaysMarque INT,
    FOREIGN KEY (id_PaysMarque) REFERENCES PaysMarques(id_PaysMarque)
);


CREATE TABLE IF NOT EXISTS Modeles (
    id_Modele SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    anneeSortie INT,
    id_Categorie INT,
    id_Marque INT,

    FOREIGN KEY (id_Marque) REFERENCES Marques(id_Marque),
    FOREIGN KEY (id_Categorie) REFERENCES Categories(id_Categorie)
);


CREATE TABLE IF NOT EXISTS Energies (
    id_Energie SERIAL PRIMARY KEY,
    nom VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS BoiteVitesses (
    id_BoiteVitesse SERIAL PRIMARY KEY,
    nom VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS EtatVoitures (
    id_EtatVoiture SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    note DOUBLE PRECISION
);


CREATE TABLE IF NOT EXISTS Utilisateurs (
    id_Utilisateur SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    dateNaissance DATE,
    dateInscription DATE,
    password VARCHAR(80),
    mail VARCHAR(60),
    contact VARCHAR(30),
    adresse VARCHAR(50),
    cin VARCHAR(30),
    role VARCHAR(30)
);


CREATE TABLE IF NOT EXISTS Annonces (
    id_Annonce SERIAL PRIMARY KEY,
    id_Utilisateur INT NOT NULL,
    id_Voiture INT NOT NULL,
    datePub DATE,
    prix DOUBLE PRECISION,
    status INT,  -- 0 en attente , 10 en cours , 20 vendu
    description VARCHAR(80),
    FOREIGN KEY (id_Utilisateur) REFERENCES Utilisateurs(id_Utilisateur)
);


CREATE TABLE IF NOT EXISTS Favoris (
    id_Favori SERIAL PRIMARY KEY,
    dateAjout DATE,
    id_Utilisateur INT,
    id_Annonce INT,
    FOREIGN KEY (id_Utilisateur) REFERENCES Utilisateurs(id_Utilisateur),
    FOREIGN KEY (id_Annonce) REFERENCES Annonces(id_Annonce)
);

CREATE TABLE IF NOT EXISTS Commissions(
    id_commission SERIAL PRIMARY KEY,
    valeur DOUBLE PRECISION,
    id_Annonce INT,

    FOREIGN KEY (id_Annonce) REFERENCES Annonces(id_Annonce)
);


select * from BoiteVitesses;
select * from Categories;
select * from Energies;
select * from EtatVoitures;
select * from Marques;
select * from modeles;
select * from PaysMarques;


select 
    sum((Commissions.valeur / 100) * Annonces.prix ) as totalBenefice
from 
    Commissions,
    Annonces 
where 
    Commissions.id_Annonce = Annonces.id_Annonce
;
 


SELECT 
    to_char(DATE_TRUNC('month', dateInscription), 'Month YYYY') AS inscription_month,
    COUNT(id_Utilisateur) AS user_count
FROM Utilisateurs
WHERE role = 'USER'
GROUP BY inscription_month
ORDER BY inscription_month;



WITH months AS ( SELECT generate_series( date_trunc('year', NOW()), date_trunc('year', NOW()) + interval '1 year - 1 day', '1 month'::interval ) AS month ) SELECT to_char(m.month, 'Month YYYY') AS inscription_month, COALESCE(COUNT(u.id_Utilisateur), 0) AS user_count FROM months m LEFT JOIN Utilisateurs u ON date_trunc('month', u.dateInscription) = m.month AND u.role = 'USER' GROUP BY m.month ORDER BY m.month;


CREATE VIEW MonthCount as 
    WITH months AS ( 
        SELECT generate_series( 
            date_trunc('year', NOW()), 
            date_trunc('year', NOW()) + INTERVAL '1 year - 1 day', 
            INTERVAL '1 month'
        ) AS month 
    ) 
    SELECT 
        ROW_NUMBER() OVER (ORDER BY m.month) AS id,
        to_char(m.month, 'Month YYYY') AS nom, 
        COALESCE(COUNT(u.id_Utilisateur), 0) AS nb 
    FROM 
        months m 
    LEFT JOIN 
        Utilisateurs u 
    ON 
        date_trunc('month', u.dateInscription) = m.month AND u.role = 'USER' 
    GROUP BY 
        m.month 
    ORDER BY 
        m.month;



CREATE VIEW AnnonceCount as 
    WITH months AS ( 
        SELECT generate_series( 
            date_trunc('year', NOW()), 
            date_trunc('year', NOW()) + INTERVAL '1 year - 1 day', 
            INTERVAL '1 month'
        ) AS month 
    ) 
    SELECT 
        ROW_NUMBER() OVER (ORDER BY m.month) AS id,
        to_char(m.month, 'Month YYYY') AS nom, 
        COALESCE(COUNT(a.id_Annonce), 0) AS nb 
    FROM 
        months m 
    LEFT JOIN 
        Annonces a 
    ON 
        date_trunc('month', a.datePub) = m.month 
    GROUP BY 
        m.month 
    ORDER BY 
        m.month;


-- jdbc:postgresql://ep-small-waterfall-a5zgvypk.us-east-2.aws.neon.tech/finaldb?user=JordanRas&password=P8sZIl7LijnV&sslmode=require