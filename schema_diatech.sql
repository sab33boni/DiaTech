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

-- ---------------------------------------------------------
-- TABELLA: carrello
-- Un carrello per ogni utente registrato.
-- Gli utenti anonimi hanno il carrello solo in sessione HTTP
-- ---------------------------------------------------------
CREATE TABLE carrello (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    id_utente  INT NOT NULL UNIQUE,
    FOREIGN KEY (id_utente) REFERENCES utente(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- TABELLA: riga_carrello
-- Ogni riga rappresenta un prodotto nel carrello con quantità
-- ---------------------------------------------------------
CREATE TABLE riga_carrello (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_carrello  INT NOT NULL,
    id_prodotto  INT NOT NULL,
    quantita     INT NOT NULL CHECK (quantita > 0),
    FOREIGN KEY (id_carrello) REFERENCES carrello(id) ON DELETE CASCADE,
    FOREIGN KEY (id_prodotto) REFERENCES prodotto(id)
);

-- ---------------------------------------------------------
-- TABELLA: ordine
-- Rappresenta un ordine completato da un cliente.
-- Salva indirizzo e metodo di pagamento al momento dell'acquisto
-- ---------------------------------------------------------
CREATE TABLE ordine (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    id_utente            INT            NOT NULL,
    data_ordine          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    stato                ENUM('IN_LAVORAZIONE', 'SPEDITO', 'CONSEGNATO', 'ANNULLATO')
                                        NOT NULL DEFAULT 'IN_LAVORAZIONE',
    totale               DECIMAL(10,2)  NOT NULL CHECK (totale >= 0),
    indirizzo_spedizione VARCHAR(255)   NOT NULL,
    citta                VARCHAR(100)   NOT NULL,
    cap                  VARCHAR(10)    NOT NULL,
    metodo_pagamento     VARCHAR(50)    NOT NULL,
    FOREIGN KEY (id_utente) REFERENCES utente(id) ON DELETE RESTRICT
);

-- ---------------------------------------------------------
-- TABELLA: riga_ordine
-- Ogni riga = un prodotto acquistato con la sua quantità.
-- prezzo_unitario è il prezzo AL MOMENTO dell'acquisto:
-- se il prezzo del prodotto cambia domani, questa riga
-- conserva il prezzo originale pagato dal cliente (prezzo congelato)
-- ---------------------------------------------------------
CREATE TABLE riga_ordine (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_ordine       INT            NOT NULL,
    id_prodotto     INT            NOT NULL,
    quantita        INT            NOT NULL CHECK (quantita > 0),
    prezzo_unitario DECIMAL(10,2)  NOT NULL CHECK (prezzo_unitario >= 0),
    FOREIGN KEY (id_ordine)   REFERENCES ordine(id)  ON DELETE CASCADE,
    FOREIGN KEY (id_prodotto) REFERENCES prodotto(id) ON DELETE RESTRICT
);

-- ---------------------------------------------------------
-- TABELLA: garanzia
-- Ogni riga d'ordine può generare una garanzia legale.
-- Collegata 1-a-1 con riga_ordine (UNIQUE su id_riga_ordine)
-- ---------------------------------------------------------
CREATE TABLE garanzia (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_riga_ordine INT  NOT NULL UNIQUE,
    data_scadenza  DATE NOT NULL,
    stato          ENUM('ATTIVA', 'SCADUTA') NOT NULL DEFAULT 'ATTIVA',
    FOREIGN KEY (id_riga_ordine) REFERENCES riga_ordine(id) ON DELETE CASCADE
);

-- =========================================================
--  DATI INIZIALI
-- =========================================================

-- ---------------------------------------------------------
-- Categorie di prodotti
-- ---------------------------------------------------------
INSERT INTO categoria (id, nome) VALUES
(1, 'Sensori CGM'),
(2, 'Glucometri'),
(3, 'Strisce Reattive'),
(4, 'Pungidito e Lancette'),
(5, 'Accessori CGM');

-- ---------------------------------------------------------
-- Brand / Produttori
-- ---------------------------------------------------------
INSERT INTO brand (id, nome) VALUES
(1, 'Abbott'),
(2, 'Roche'),
(3, 'Ascensia'),
(4, 'Menarini'),
(5, 'Dexcom');
