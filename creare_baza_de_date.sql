CREATE DATABASE IF NOT EXISTS donor_db;
USE donor_db;

-- 1. Tabela pentru adrese
CREATE TABLE adresa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    judet VARCHAR(50) NOT NULL,
    localitate VARCHAR(50) NOT NULL,
    strada VARCHAR(100),
    numar INT,
    cod_postal VARCHAR(6)
);

-- 2. Tabela principală pentru utilizatori
CREATE TABLE utilizator (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    parola VARCHAR(255) NOT NULL,
    nr_telefon VARCHAR(10),
    nume VARCHAR(50) NOT NULL,
    prenume VARCHAR(50) NOT NULL,
    rol ENUM('ADMIN', 'DONATOR', 'MEDIC', 'BIOLOG') NOT NULL
);

-- 3. Tabela pentru donatori
-- Am schimbat '0' in 'ZERO', '+' in 'POZITIV' si '-' in 'NEGATIV'
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

-- 4. Tabela pentru administratori
CREATE TABLE administrator (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE
);

-- 5. Tabela pentru medici
CREATE TABLE medic (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    cod_parafa VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE
);

-- 6. Tabela pentru biologi
CREATE TABLE biolog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilizator INT NOT NULL UNIQUE,
    cod_parafa VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_utilizator) REFERENCES utilizator(id) ON DELETE CASCADE
);

-- 7. Tabela pentru programări
CREATE TABLE programare (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_donator INT NOT NULL,
    data_ora_programare DATETIME NOT NULL,
    status ENUM('CONFIRMATA', 'ANULATA', 'FINALIZATA', 'RESPINSA') DEFAULT 'CONFIRMATA',
    FOREIGN KEY (id_donator) REFERENCES donator(id) ON DELETE CASCADE
);

-- 8. Tabela pentru evidența donării
CREATE TABLE donare (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_donator INT NOT NULL,
    data_donare DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_donator) REFERENCES donator(id) ON DELETE CASCADE
);

-- 9. Tabela pentru rezultatele analizelor
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

-- 10. Tabela pentru stocul de sânge
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