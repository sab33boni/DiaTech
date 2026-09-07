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

-- ---------------------------------------------------------
-- TABELLA: prodotto
-- Catalogo dei dispositivi medici in vendita
-- cancellato = soft-delete: il prodotto sparisce dal
-- catalogo ma rimane nei vecchi ordini (integrità storica)
-- ---------------------------------------------------------
CREATE TABLE prodotto (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    nome                 VARCHAR(150)   NOT NULL,
    descrizione          TEXT,
    prezzo               DECIMAL(10,2)  NOT NULL CHECK (prezzo >= 0),
    quantita_disponibile INT            NOT NULL DEFAULT 0 CHECK (quantita_disponibile >= 0),
    immagine             VARCHAR(255)   DEFAULT 'default.png',
    id_categoria         INT            NOT NULL,
    id_brand             INT            NOT NULL,
    cancellato           BOOLEAN        NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id),
    FOREIGN KEY (id_brand)     REFERENCES brand(id)
);

-- ---------------------------------------------------------
-- TABELLA: prodotto_compatibile
-- Relazione molti-a-molti: quali prodotti sono compatibili
-- tra loro (es. sensore CGM + patch adesive)
-- La coppia (id_prodotto_1, id_prodotto_2) è la PK composita
-- ---------------------------------------------------------
CREATE TABLE prodotto_compatibile (
    id_prodotto_1 INT NOT NULL,
    id_prodotto_2 INT NOT NULL,
    PRIMARY KEY (id_prodotto_1, id_prodotto_2),
    FOREIGN KEY (id_prodotto_1) REFERENCES prodotto(id),
    FOREIGN KEY (id_prodotto_2) REFERENCES prodotto(id)
);
