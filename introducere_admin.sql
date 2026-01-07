-- Pasul 1: Inserarea în tabela utilizator
-- Parola de mai jos este hash-ul pentru: admin123
INSERT INTO utilizator (email, parola, nr_telefon, nume, prenume, rol) 
VALUES (
    'admin@donor.ro', 
    '$2a$10$8.VAgUnF9hXz.P3M1S.qHeX.Mh.i2rYy.M6g/f.W6E7M8W9qG1S2i', 
    '0772255246', 
    'Admin', 
    'Sistem', 
    'ADMIN'
);

-- Pasul 2: Inserarea în tabela administrator
-- Folosim o subinterogare pentru a lua automat ID-ul generat anterior
INSERT INTO administrator (id_utilizator) 
SELECT id FROM utilizator WHERE email = 'admin@donor.ro';