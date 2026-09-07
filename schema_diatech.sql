-- =========================================================
--  DiaTech Solutions — Schema Database MySQL
--  Corso: Tecnologie e Sistemi Web — UNISA
--  Creato: 07/09/2026
-- =========================================================

-- Creiamo e selezioniamo il database
DROP DATABASE IF EXISTS diatech;
CREATE DATABASE diatech CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE diatech;

-- ---------------------------------------------------------
-- TABELLA: categoria
-- Contiene le categorie dei prodotti (es. Sensori CGM, Glucometri)
-- ---------------------------------------------------------
CREATE TABLE categoria (
    id   INT AUTO_INCREMENT PRIMARY KEY,  -- ID univoco autoincrementale
    nome VARCHAR(100) NOT NULL            -- Nome della categoria
);

-- ---------------------------------------------------------
-- TABELLA: brand
-- Contiene i marchi/produttori (es. Abbott, Roche)
-- ---------------------------------------------------------
CREATE TABLE brand (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------
-- TABELLA: utente
-- Contiene sia i clienti registrati che gli amministratori
-- ---------------------------------------------------------
CREATE TABLE utente (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nome                VARCHAR(100)  NOT NULL,
    cognome             VARCHAR(100)  NOT NULL,
    email               VARCHAR(150)  NOT NULL UNIQUE,
    password_hash       VARCHAR(255)  NOT NULL,
    indirizzo           VARCHAR(255),
    citta               VARCHAR(100),
    cap                 VARCHAR(10),
    telefono            VARCHAR(20),
    ruolo               ENUM('REGISTRATO', 'ADMIN') NOT NULL DEFAULT 'REGISTRATO',
    data_registrazione  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
