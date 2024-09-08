-- MySQL dump 10.19  Distrib 10.3.39-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: recipes
-- ------------------------------------------------------
-- Server version	10.3.39-MariaDB-1:10.3.39+maria~ubu2004

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `recipe_likes`
--
CREATE DATABASE IF NOT EXISTS `recipes` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `recipes`;

DROP TABLE IF EXISTS `recipe_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipe_likes` (
  `recipe_id` bigint(20) NOT NULL,
  `user_uri` varchar(255) DEFAULT NULL,
  KEY `FK123rwitbkijue4y39x2a9pqqb` (`recipe_id`),
  CONSTRAINT `FK123rwitbkijue4y39x2a9pqqb` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe_likes`
--

LOCK TABLES `recipe_likes` WRITE;
/*!40000 ALTER TABLE `recipe_likes` DISABLE KEYS */;
INSERT INTO `recipe_likes` VALUES (2,'/users/id/1'),(2,'/users/id/3');
/*!40000 ALTER TABLE `recipe_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe_product_quantities`
--

DROP TABLE IF EXISTS `recipe_product_quantities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipe_product_quantities` (
  `recipe_id` bigint(20) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `product_uri` varchar(255) NOT NULL,
  PRIMARY KEY (`recipe_id`,`product_uri`),
  CONSTRAINT `FK5yehk13qrdc6ssdwjbb1lgtht` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe_product_quantities`
--

LOCK TABLES `recipe_product_quantities` WRITE;
/*!40000 ALTER TABLE `recipe_product_quantities` DISABLE KEYS */;
INSERT INTO `recipe_product_quantities` VALUES (2,1,'/products/1'),(2,2,'/products/2'),(2,3,'/products/3'),(2,4,'/products/4'),(3,4,'/products/1335'),(3,400,'/products/1411'),(3,500,'/products/207'),(3,250,'/products/52'),(3,4,'/products/583'),(3,650,'/products/835'),(3,8,'/products/86'),(4,100,'/products/1014'),(4,8,'/products/1335'),(4,250,'/products/207'),(4,30,'/products/619'),(4,250,'/products/646'),(4,75,'/products/835');
/*!40000 ALTER TABLE `recipe_product_quantities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe_products`
--

DROP TABLE IF EXISTS `recipe_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipe_products` (
  `recipe_id` bigint(20) NOT NULL,
  `product_uri` varchar(255) DEFAULT NULL,
  KEY `FKssj43103hudcp3u3x7b8twnd5` (`recipe_id`),
  CONSTRAINT `FKssj43103hudcp3u3x7b8twnd5` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe_products`
--

LOCK TABLES `recipe_products` WRITE;
/*!40000 ALTER TABLE `recipe_products` DISABLE KEYS */;
INSERT INTO `recipe_products` VALUES (2,'/products/1'),(2,'/products/2'),(2,'/products/3'),(2,'/products/4'),(3,'/products/207'),(3,'/products/1411'),(3,'/products/583'),(3,'/products/1335'),(3,'/products/86'),(3,'/products/835'),(3,'/products/52'),(4,'/products/207'),(4,'/products/646'),(4,'/products/1014'),(4,'/products/835'),(4,'/products/619'),(4,'/products/1335');
/*!40000 ALTER TABLE `recipe_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipes` (
  `recipe_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `recipe_creation_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `recipe_description` varchar(10000) DEFAULT NULL,
  `recipe_image_uri` varchar(255) DEFAULT NULL,
  `recipe_is_private` bit(1) DEFAULT NULL,
  `recipe_owner_uri` varchar(255) DEFAULT NULL,
  `recipe_title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`recipe_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipes`
--

LOCK TABLES `recipes` WRITE;
/*!40000 ALTER TABLE `recipes` DISABLE KEYS */;
INSERT INTO `recipes` VALUES (3,'2024-09-08 21:12:32','205g Butter, 200g Zucker, 250g Mehl mit den Eiern, Vanillezucker und Backpulver vermengen und auf ein Backblech schütten.\nAus dem restlichen Mehl, Zucker und Butter streusel kneten.\nDen Teig mit Apfelmus beschmieren und die Streusel verteilen.\n\nBei 200°C für 40 min backen','https://www.foodio.de/wp-content/uploads/2020/06/Apfelmuskuchen-vom-Blech-mit-Streusel_14234_portrait-1300x1000.jpg','\0','/users/id/1','Apfelmuskuchen'),(4,'2024-09-08 21:22:06','Alle Zutaten verkneten und in kleine Kugel rollen.\n\nBei mittlerer Hitz 12 - 15 min backen\nNach dem Abkühlen mit Puderzucker bestreuen','https://img.chefkoch-cdn.de/rezepte/1504591255940677/bilder/1079783/crop-960x720/schokoli.jpg','\0','/users/id/1','Schokolie');
/*!40000 ALTER TABLE `recipes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-08 21:25:23
