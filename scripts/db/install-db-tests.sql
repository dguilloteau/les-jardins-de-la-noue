CREATE OR REPLACE DATABASE `les-jardins-de-la-noue` /*!40100 COLLATE 'utf8mb4_general_ci' */
CREATE USER 'LesJardinsDeLaNoue'@'localhost' IDENTIFIED VIA mysql_native_password USING 'ljdln@2025';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, INDEX, ALTER ON *.* TO 'LesJardinsDeLaNoue'@'localhost'; 