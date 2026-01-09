-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: donor_db
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_utilizator` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_utilizator` (`id_utilizator`),
  CONSTRAINT `administrator_ibfk_1` FOREIGN KEY (`id_utilizator`) REFERENCES `utilizator` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (4,19);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adresa`
--

DROP TABLE IF EXISTS `adresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adresa` (
  `id` int NOT NULL AUTO_INCREMENT,
  `judet` varchar(255) DEFAULT NULL,
  `localitate` varchar(255) DEFAULT NULL,
  `strada` varchar(255) DEFAULT NULL,
  `numar` int DEFAULT NULL,
  `cod_postal` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adresa`
--

LOCK TABLES `adresa` WRITE;
/*!40000 ALTER TABLE `adresa` DISABLE KEYS */;
INSERT INTO `adresa` VALUES (6,'Sibiu','Sibiu','Bd. M. Viteazu',15,'550368'),(7,'Sibiu','Sibiu','Bd. M. Viteazu',15,'550126'),(8,'Cluj','Cluj-Napoca','Calea Turzii',120,'469785'),(9,'Cluj','Cluj-Napoca','Str. Republicii',25,'980456'),(10,'Cluj','Cluj-Napoca','Calea Motilor',45,'754023'),(11,'Alba','Alba-Iulia','Str. Vasile Goldis',54,'556425'),(12,'Cluj','Cluj-Napoca','Str. George Baritiu',17,'550366'),(13,'Bihor','Oradea','Str. Panselutelor',100,'456789'),(14,'Cluj','Cluj-Napoca','Str. Emil Isac',56,'201564');
/*!40000 ALTER TABLE `adresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alerta`
--

DROP TABLE IF EXISTS `alerta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alerta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `grupa_sanguina` enum('A','B','AB','ZERO') NOT NULL,
  `rh` enum('POZITIV','NEGATIV') NOT NULL,
  `titlu_mesaj` varchar(255) NOT NULL,
  `continut_mesaj` text NOT NULL,
  `data_ora` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerta`
--

LOCK TABLES `alerta` WRITE;
/*!40000 ALTER TABLE `alerta` DISABLE KEYS */;
INSERT INTO `alerta` VALUES (6,'ZERO','POZITIV','Urgență sânge - grupa 0 RH POZITIV','Avem nevoie urgentă de sânge grupa 0, RH POZITIV. Vă rugăm să vă prezentați la centrul de sânge dacă aveți posibilitatea!','2026-01-09 18:36:41'),(7,'ZERO','POZITIV','Urgență - grupa 0, RH +','Avem nevoie urgentă de sânge! Prezentați-vă la centru!','2026-01-09 18:40:37'),(8,'ZERO','NEGATIV','Urgență! Grupa 0, Rh negativ','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:44:14'),(9,'A','POZITIV','Urgență sânge - grupa A, RH Pozitiv','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:45:02'),(10,'A','NEGATIV','Urgență sânge - Grupa A, RH NEGATIV','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:45:57'),(11,'B','POZITIV','Urgență de sânge! Grupa B, RH POZITIV','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:46:33'),(12,'B','NEGATIV','Urgență de sânge! Grupa B, RH NEGATIV','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:47:18'),(13,'AB','POZITIV','Urgență de sânge - grupa AB, RH POZITIV','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:49:10'),(14,'AB','NEGATIV','Urgență sânge - Grupa AB, RH NEGATIV','Avem nevoie urgentă de sânge! Vă rog să vă prezentați la centru!','2026-01-09 18:49:47');
/*!40000 ALTER TABLE `alerta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `analiza_sange`
--

DROP TABLE IF EXISTS `analiza_sange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `analiza_sange` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_donare` int NOT NULL,
  `data_introducere_rezultat` datetime DEFAULT NULL,
  `cantitate_ml` int DEFAULT NULL,
  `grupa_sanguina` enum('A','B','AB','ZERO') DEFAULT NULL,
  `rh` enum('POZITIV','NEGATIV') DEFAULT NULL,
  `rezultat` enum('IN_ASTEPTARE','ADMIS','RESPINS') DEFAULT 'IN_ASTEPTARE',
  `mesaj` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_donare` (`id_donare`),
  CONSTRAINT `analiza_sange_ibfk_1` FOREIGN KEY (`id_donare`) REFERENCES `donare` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `analiza_sange`
--

LOCK TABLES `analiza_sange` WRITE;
/*!40000 ALTER TABLE `analiza_sange` DISABLE KEYS */;
INSERT INTO `analiza_sange` VALUES (10,10,'2026-01-14 09:15:00',450,'ZERO','POZITIV','ADMIS','Mulțumim pentru donare!'),(11,11,'2026-03-06 11:15:00',450,'ZERO','POZITIV','ADMIS','Mulțumim pentru donare!'),(12,12,'2026-06-05 10:15:00',500,'ZERO','POZITIV','ADMIS','Mulțumim pentru donare!'),(13,13,'2026-02-19 11:15:00',400,'ZERO','NEGATIV','ADMIS','Mulțumim pentru donare!'),(14,14,'2026-04-09 10:15:00',350,'ZERO','NEGATIV','ADMIS','Mulțumim pentru donare!'),(15,15,'2026-10-08 12:15:00',400,'ZERO','NEGATIV','RESPINS','Sânge contaminat - Hepatită! Vă rugăm să vă prezentați la centru!'),(16,16,'2026-02-11 08:15:00',500,'A','POZITIV','ADMIS','Mulțumim pentru donare!'),(17,17,'2026-02-03 10:15:00',400,'A','NEGATIV','ADMIS','Mulțumim pentru donare!'),(18,18,'2026-01-17 10:15:00',550,'B','POZITIV','ADMIS','Mulțumim pentru donare!'),(19,19,NULL,NULL,NULL,NULL,'IN_ASTEPTARE',NULL),(20,20,'2026-02-12 10:15:00',500,'B','NEGATIV','ADMIS','Mulțumim pentru donare!'),(21,21,'2026-03-05 11:15:00',550,'AB','POZITIV','ADMIS','Mulțumim pentru donare!'),(22,22,'2026-05-06 11:15:00',540,'AB','NEGATIV','ADMIS','Mulțumim pentru donare!');
/*!40000 ALTER TABLE `analiza_sange` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biolog`
--

DROP TABLE IF EXISTS `biolog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `biolog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_utilizator` int NOT NULL,
  `cod_parafa` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_utilizator` (`id_utilizator`),
  CONSTRAINT `biolog_ibfk_1` FOREIGN KEY (`id_utilizator`) REFERENCES `utilizator` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biolog`
--

LOCK TABLES `biolog` WRITE;
/*!40000 ALTER TABLE `biolog` DISABLE KEYS */;
INSERT INTO `biolog` VALUES (6,23,'BIO-98'),(7,24,'BIO-190');
/*!40000 ALTER TABLE `biolog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donare`
--

DROP TABLE IF EXISTS `donare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donare` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_donator` int NOT NULL,
  `data_donare` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id_donator` (`id_donator`),
  CONSTRAINT `donare_ibfk_1` FOREIGN KEY (`id_donator`) REFERENCES `donator` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donare`
--

LOCK TABLES `donare` WRITE;
/*!40000 ALTER TABLE `donare` DISABLE KEYS */;
INSERT INTO `donare` VALUES (10,7,'2026-01-13 09:15:00'),(11,7,'2026-03-05 11:15:00'),(12,7,'2026-06-04 10:15:00'),(13,8,'2026-02-18 11:15:00'),(14,8,'2026-04-08 10:15:00'),(15,8,'2026-10-07 12:15:00'),(16,9,'2026-02-10 08:15:00'),(17,10,'2026-02-02 10:15:00'),(18,11,'2026-01-16 10:15:00'),(19,11,'2026-04-02 08:15:00'),(20,12,'2026-02-11 10:15:00'),(21,13,'2026-03-04 11:15:00'),(22,14,'2026-05-05 11:15:00');
/*!40000 ALTER TABLE `donare` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donator`
--

DROP TABLE IF EXISTS `donator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_utilizator` int NOT NULL,
  `id_adresa` int DEFAULT NULL,
  `cnp` varchar(255) NOT NULL,
  `data_nasterii` datetime DEFAULT NULL,
  `varsta` int DEFAULT NULL,
  `sex` enum('M','F') DEFAULT NULL,
  `greutate` float DEFAULT NULL,
  `inaltime` float DEFAULT NULL,
  `grupa_sanguina` enum('A','B','AB','ZERO') DEFAULT NULL,
  `rh` enum('POZITIV','NEGATIV') DEFAULT NULL,
  `status` enum('ELIGIBIL','INELIGIBIL_TEMPORAR','INELIGIBIL_PERMANENT') DEFAULT 'ELIGIBIL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_utilizator` (`id_utilizator`),
  UNIQUE KEY `cnp` (`cnp`),
  KEY `id_adresa` (`id_adresa`),
  CONSTRAINT `donator_ibfk_1` FOREIGN KEY (`id_utilizator`) REFERENCES `utilizator` (`id`) ON DELETE CASCADE,
  CONSTRAINT `donator_ibfk_2` FOREIGN KEY (`id_adresa`) REFERENCES `adresa` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donator`
--

LOCK TABLES `donator` WRITE;
/*!40000 ALTER TABLE `donator` DISABLE KEYS */;
INSERT INTO `donator` VALUES (7,26,7,'5041016548965','2004-10-16 00:00:00',21,'M',80,1.81,'ZERO','POZITIV','ELIGIBIL'),(8,27,8,'5900116456789','1990-01-16 00:00:00',35,'M',100,1.95,'ZERO','NEGATIV','INELIGIBIL_PERMANENT'),(9,28,9,'6000212657894','2000-02-12 00:00:00',25,'F',68,1.72,'A','POZITIV','ELIGIBIL'),(10,29,10,'2800522123457','1980-05-22 00:00:00',45,'F',70,1.75,'A','NEGATIV','ELIGIBIL'),(11,30,11,'1950314123459','1995-03-14 00:00:00',30,'M',105,2,'B','POZITIV','INELIGIBIL_TEMPORAR'),(12,31,12,'5010719123454','2001-07-19 00:00:00',24,'M',59,1.68,'B','NEGATIV','ELIGIBIL'),(13,32,13,'1961023123458','1996-10-23 00:00:00',29,'M',90,1.85,'AB','POZITIV','ELIGIBIL'),(14,33,14,'2990415123451','1999-04-15 00:00:00',26,'F',65,1.72,'AB','NEGATIV','ELIGIBIL');
/*!40000 ALTER TABLE `donator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medic`
--

DROP TABLE IF EXISTS `medic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_utilizator` int NOT NULL,
  `cod_parafa` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_utilizator` (`id_utilizator`),
  CONSTRAINT `medic_ibfk_1` FOREIGN KEY (`id_utilizator`) REFERENCES `utilizator` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medic`
--

LOCK TABLES `medic` WRITE;
/*!40000 ALTER TABLE `medic` DISABLE KEYS */;
INSERT INTO `medic` VALUES (7,21,'MED-178'),(8,22,'MED-116');
/*!40000 ALTER TABLE `medic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programare`
--

DROP TABLE IF EXISTS `programare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programare` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_donator` int NOT NULL,
  `data_ora_programare` datetime NOT NULL,
  `status` enum('CONFIRMATA','ANULATA','FINALIZATA','RESPINSA') DEFAULT 'CONFIRMATA',
  PRIMARY KEY (`id`),
  KEY `id_donator` (`id_donator`),
  CONSTRAINT `programare_ibfk_1` FOREIGN KEY (`id_donator`) REFERENCES `donator` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programare`
--

LOCK TABLES `programare` WRITE;
/*!40000 ALTER TABLE `programare` DISABLE KEYS */;
INSERT INTO `programare` VALUES (21,7,'2026-01-13 09:00:00','FINALIZATA'),(22,7,'2026-03-05 11:00:00','FINALIZATA'),(23,7,'2026-06-04 10:00:00','ANULATA'),(24,7,'2026-06-04 10:00:00','FINALIZATA'),(25,8,'2026-02-18 11:00:00','FINALIZATA'),(26,8,'2026-04-08 10:00:00','FINALIZATA'),(27,8,'2026-10-07 12:00:00','FINALIZATA'),(28,9,'2026-02-10 08:00:00','FINALIZATA'),(29,9,'2026-05-07 08:00:00','CONFIRMATA'),(30,10,'2026-02-02 10:00:00','FINALIZATA'),(31,10,'2026-05-21 11:00:00','RESPINSA'),(32,11,'2026-01-16 10:00:00','FINALIZATA'),(33,11,'2026-04-02 08:00:00','FINALIZATA'),(34,12,'2026-02-11 10:00:00','FINALIZATA'),(35,13,'2026-03-04 11:00:00','FINALIZATA'),(36,14,'2026-05-05 11:00:00','FINALIZATA');
/*!40000 ALTER TABLE `programare` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stoc_sange`
--

DROP TABLE IF EXISTS `stoc_sange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stoc_sange` (
  `id` int NOT NULL AUTO_INCREMENT,
  `grupa_sanguina` enum('A','B','AB','ZERO') NOT NULL,
  `rh` enum('POZITIV','NEGATIV') NOT NULL,
  `cantitate_ml` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stoc_sange`
--

LOCK TABLES `stoc_sange` WRITE;
/*!40000 ALTER TABLE `stoc_sange` DISABLE KEYS */;
INSERT INTO `stoc_sange` VALUES (1,'ZERO','POZITIV',1100),(2,'ZERO','NEGATIV',750),(3,'A','POZITIV',500),(4,'A','NEGATIV',400),(5,'B','POZITIV',550),(6,'B','NEGATIV',500),(7,'AB','POZITIV',550),(8,'AB','NEGATIV',540);
/*!40000 ALTER TABLE `stoc_sange` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilizator`
--

DROP TABLE IF EXISTS `utilizator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilizator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `parola` varchar(255) NOT NULL,
  `nr_telefon` varchar(255) DEFAULT NULL,
  `nume` varchar(255) DEFAULT NULL,
  `prenume` varchar(255) DEFAULT NULL,
  `rol` enum('ADMIN','DONATOR','MEDIC','BIOLOG') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilizator`
--

LOCK TABLES `utilizator` WRITE;
/*!40000 ALTER TABLE `utilizator` DISABLE KEYS */;
INSERT INTO `utilizator` VALUES (19,'admin@donor.ro','$2a$10$cORT34vPQn/ncmc8t6ymQOpGlmx9A0gfSgzaX4b7NHZFtmVSaGX2W','0745245635','Ionescu','Andrei','ADMIN'),(21,'medic@donor.ro','$2a$10$XWuzlv1DMMc8wJ5lXygXfe040tfejQGe1dgROlR8y5f64QpPyDs4S','0752645626','Popescu','Matei','MEDIC'),(22,'medic2@donor.ro','$2a$10$9Ds0qoJ0rnUP3.QJyiBS9Ofa/tfFHCyb/loOIbE6IfCA0jGkTmFqy','0756412659','Muresan','Ioan','MEDIC'),(23,'biolog@donor.ro','$2a$10$NVz76SvVkc5CxxmS1o.3Qu1JTG9PCEv6vYE.htYYeoDUqxK672AI2','0795426482','Cristescu','Maria','BIOLOG'),(24,'biolog2@donor.ro','$2a$10$Ax/OwiN6THKhGA2s7SinqOZ1AVKU1VQoojTGqTnpxKCteke3idPpK','0724565984','Nistor','Ana','BIOLOG'),(26,'andrei.popa@gmail.com','$2a$10$gHKyPCkdJLXO/Bzc5IwQJu3bP6/RR3bTl.uIkReTdz6hIxzM4W766','0752648654','Popa','Andrei','DONATOR'),(27,'marian@yahoo.com','$2a$10$SseM/MQsHfd44A6IobO9QOZZZPKhnZ/F.mTkBworWGN6MtauUSGvO','0775566459','Cosman','Marian','DONATOR'),(28,'elena@outlook.com','$2a$10$F6mOz51Nb/tra4nuYdulCODIk2Q.eDqKzPTyQRJTO7MVuQQdNsNrm','0758961456','Costache','Elena','DONATOR'),(29,'raluca@gmail.com','$2a$10$nzt9Awtt0VpdH7ivFs7e/ObnXzjzkn.QlqXNrTAJK77PO8sFMRY6C','0756245368','Bejan','Raluca','DONATOR'),(30,'stanciu@gmail.com','$2a$10$5pM3uqnCq66gt7/oCciiv.h1/b6y7f2Uv5wxxZR3reZzcpbX6wXAK','0751236459','Stanciu','Nicolae','DONATOR'),(31,'marius@gmail.com','$2a$10$zmPTPPwrMbaASHte5.WwTuRticLjRpAwmgeNVxRT7zNWsJqikfPQG','0772255489','Racovitan','Marius','DONATOR'),(32,'george@gmail.com','$2a$10$Hd.4H9jG1pwec56X4yDbBuvhWftXMFXs3CgUjTnQPzmHqDftEUeJm','0764526482','Miculescu','George','DONATOR'),(33,'mihaela@gmail.com','$2a$10$71r72LNkIa1PEilBsFiDYuENpPZwQsHeY3eMA/8ywX1Aj2PkLu6Fe','0756215482','Olimpiu','Mihaela','DONATOR');
/*!40000 ALTER TABLE `utilizator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'donor_db'
--

--
-- Dumping routines for database 'donor_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-09 20:48:47
