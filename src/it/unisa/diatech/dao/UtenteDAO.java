package it.unisa.diatech.dao;

import it.unisa.diatech.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class UtenteDAO {

    private static final String TABLE_NAME = "utente";

    
    public Utente doRetrieveByKey(int id) throws SQLException, NamingException {
        String query = "SELECT id, nome, cognome, email, password_hash, indirizzo, citta, cap, telefono, ruolo, data_registrazione "
                     + "FROM " + TABLE_NAME + " WHERE id = ?";
        Utente u = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = mapUtente(rs);
                }
            }
        }
        return u;
    }

    
    public Utente doRetrieveByEmail(String email) throws SQLException, NamingException {
        String query = "SELECT id, nome, cognome, email, password_hash, indirizzo, citta, cap, telefono, ruolo, data_registrazione "
                     + "FROM " + TABLE_NAME + " WHERE LOWER(email) = ?";
        Utente u = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email != null ? email.trim().toLowerCase() : "");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = mapUtente(rs);
                }
            }
        }
        return u;
    }

    
    public Utente doRetrieveByEmailAndPassword(String email, String passwordHash) throws SQLException, NamingException {
        String query = "SELECT id, nome, cognome, email, password_hash, indirizzo, citta, cap, telefono, ruolo, data_registrazione "
                     + "FROM " + TABLE_NAME + " WHERE LOWER(email) = ? AND password_hash = ?";
        Utente u = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email != null ? email.trim().toLowerCase() : "");
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = mapUtente(rs);
                }
            }
        }
        return u;
    }

    
    public boolean checkEmailExists(String email) throws SQLException, NamingException {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE LOWER(email) = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email != null ? email.trim().toLowerCase() : "");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    
    public synchronized int doSave(Utente utente) throws SQLException, NamingException {
        String query = "INSERT INTO " + TABLE_NAME + " (nome, cognome, email, password_hash, indirizzo, citta, cap, telefono, ruolo) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail().trim().toLowerCase());
            ps.setString(4, utente.getPasswordHash());
            ps.setString(5, utente.getIndirizzo());
            ps.setString(6, utente.getCitta());
            ps.setString(7, utente.getCap());
            ps.setString(8, utente.getTelefono());
            ps.setString(9, utente.getRuolo() != null ? utente.getRuolo() : "REGISTRATO");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    utente.setId(generatedId);
                }
            }
        }
        return generatedId;
    }

    
    public synchronized boolean doUpdate(Utente utente) throws SQLException, NamingException {
        String query = "UPDATE " + TABLE_NAME + " SET nome = ?, cognome = ?, indirizzo = ?, citta = ?, cap = ?, telefono = ? "
                     + "WHERE id = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getIndirizzo());
            ps.setString(4, utente.getCitta());
            ps.setString(5, utente.getCap());
            ps.setString(6, utente.getTelefono());
            ps.setInt(7, utente.getId());

            return ps.executeUpdate() > 0;
        }
    }

    
    public List<Utente> doRetrieveAllClienti() throws SQLException, NamingException {
        List<Utente> clienti = new ArrayList<>();
        String query = "SELECT id, nome, cognome, email, password_hash, indirizzo, citta, cap, telefono, ruolo, data_registrazione "
                     + "FROM " + TABLE_NAME + " WHERE ruolo = 'REGISTRATO' ORDER BY cognome ASC, nome ASC";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clienti.add(mapUtente(rs));
            }
        }
        return clienti;
    }

    private Utente mapUtente(ResultSet rs) throws SQLException {
        Utente u = new Utente();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setCognome(rs.getString("cognome"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setIndirizzo(rs.getString("indirizzo"));
        u.setCitta(rs.getString("citta"));
        u.setCap(rs.getString("cap"));
        u.setTelefono(rs.getString("telefono"));
        u.setRuolo(rs.getString("ruolo"));
        u.setDataRegistrazione(rs.getString("data_registrazione"));
        return u;
    }
}
