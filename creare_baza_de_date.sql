CREATE DATABASE IF NOT EXISTS donor_db;
USE donor_db;

CREATE TABLE adresa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    judet VARCHAR(50) NOT NULL,
    localitate VARCHAR(50) NOT NULL,
    strada VARCHAR(100),
    numar INT,
    cod_postal VARCHAR(6)
);

CREATE TABLE utilizator (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    parola VARCHAR(255) NOT NULL,
    nr_telefon VARCHAR(10),
    nume VARCHAR(50) NOT NULL,
    prenume VARCHAR(50) NOT NULL,
    rol ENUM('ADMIN', 'DONATOR', 'MEDIC', 'BIOLOG') NOT NULL
);

CREATE TABLE donator (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    id_adresa INT,
    cnp VARCHAR(13) NOT NULL UNIQUE,
    data_nasterii DATETIME,
    varsta INT,
    sex ENUM('M', 'F'),
    greutate FLOAT,
    inaltime FLOAT,
    grupa_sanguina ENUM('A', 'B', 'AB', 'ZERO'),
    rh ENUM('POZITIV', 'NEGATIV'),
    status ENUM('ELIGIBIL', 'INELIGIBIL_TEMPORAR', 'INELIGIBIL_PERMANENT') DEFAULT 'ELIGIBIL',
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE,
    FOREIGN KEY (id_adresa) REFERENCES adresa(id) ON DELETE SET NULL
);

CREATE TABLE administrator (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE
);

CREATE TABLE medic (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    cod_parafa VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE
);

CREATE TABLE biolog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    cod_parafa VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE
);

CREATE TABLE programare (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_donator INT NOT NULL,
    data_ora_programare DATETIME NOT NULL,
    status ENUM('CONFIRMATA', 'ANULATA', 'FINALIZATA', 'RESPINSA') DEFAULT 'CONFIRMATA',
    FOREIGN KEY (id_donator) REFERENCES donator(id) ON DELETE CASCADE
);

CREATE TABLE donare (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_donator INT NOT NULL,
    data_donare DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_donator) REFERENCES donator(id) ON DELETE CASCADE
);

CREATE TABLE analiza_sange (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_donare INT NOT NULL UNIQUE,
    data_introducere_rezultat DATETIME,
    cantitate_ml INT,
    grupa_sanguina ENUM('A', 'B', 'AB', 'ZERO'),
    rh ENUM('POZITIV', 'NEGATIV'),
    rezultat ENUM('IN_ASTEPTARE', 'ADMIS', 'RESPINS') DEFAULT 'IN_ASTEPTARE',
    mesaj VARCHAR(255),
    FOREIGN KEY (id_donare) REFERENCES donare(id) ON DELETE CASCADE
);

CREATE TABLE stoc_sange (
    id INT AUTO_INCREMENT PRIMARY KEY,
    grupa_sanguina ENUM('A', 'B', 'AB', 'ZERO') NOT NULL,
    rh ENUM('POZITIV', 'NEGATIV') NOT NULL,
    cantitate_ml INT NOT NULL DEFAULT 0
);

CREATE TABLE alerta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    grupa_sanguina ENUM('A', 'B', 'AB', 'ZERO') NOT NULL,
    rh ENUM('POZITIV', 'NEGATIV') NOT NULL,
    titlu_mesaj VARCHAR(255) NOT NULL,
    continut_mesaj TEXT NOT NULL,
    data_ora DATETIME DEFAULT CURRENT_TIMESTAMP
);