package it.unisa.diatech.dao;

import it.unisa.diatech.model.Brand;
import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.Categoria;
import it.unisa.diatech.model.Prodotto;
import it.unisa.diatech.model.RigaCarrello;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

/**
 * DAO per la gestione della persistenza del carrello sul database
 * (tabelle 'carrello' e 'riga_carrello').
 * Consente il salvataggio e il recupero del carrello per gli utenti registrati
 * quando effettuano il login o quando navigano tra sessioni diverse.
 */
public class CarrelloDAO {

    /**
     * Recupera il carrello salvato su DB di un utente registrato.
     * Se l'utente non ha ancora un carrello su DB, restituisce un nuovo Carrello vuoto.
     */
    public Carrello doRetrieveByUtente(int idUtente) throws SQLException, NamingException {
        Carrello carrello = new Carrello();
        carrello.setIdUtente(idUtente);

        String selectCarrelloQuery = "SELECT id FROM carrello WHERE id_utente = ?";
        int idCarrello = -1;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(selectCarrelloQuery)) {

            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idCarrello = rs.getInt("id");
                    carrello.setId(idCarrello);
                }
            }
        }

        if (idCarrello > 0) {
            carrello.setRighe(doRetrieveRigheByCarrello(idCarrello));
        }

        return carrello;
    }

    /**
     * Sincronizza/salva l'intero contenuto del carrello di sessione sul database.
     * Cancella le vecchie righe e inserisce quelle correnti in un'unica operazione transazionale.
     */
    public synchronized void doSaveOrUpdate(int idUtente, Carrello carrello) throws SQLException, NamingException {
        Connection con = null;

        try {
            con = DataSourceSingleton.getInstance().getConnection();
            con.setAutoCommit(false); // TRANSAZIONE

            // 1. Assicurati che esista il record carrello per l'utente
            int idCarrello = -1;
            String checkCarrelloQuery = "SELECT id FROM carrello WHERE id_utente = ?";
            try (PreparedStatement psCheck = con.prepareStatement(checkCarrelloQuery)) {
                psCheck.setInt(1, idUtente);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        idCarrello = rs.getInt("id");
                    }
                }
            }

            if (idCarrello == -1) {
                String insertCarrelloQuery = "INSERT INTO carrello (id_utente) VALUES (?)";
                try (PreparedStatement psInsert = con.prepareStatement(insertCarrelloQuery, Statement.RETURN_GENERATED_KEYS)) {
                    psInsert.setInt(1, idUtente);
                    psInsert.executeUpdate();
                    try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idCarrello = rsKeys.getInt(1);
                        }
                    }
                }
            }

            carrello.setId(idCarrello);
            carrello.setIdUtente(idUtente);

            // 2. Rimuovi le vecchie righe del carrello
            String deleteRigheQuery = "DELETE FROM riga_carrello WHERE id_carrello = ?";
            try (PreparedStatement psDelete = con.prepareStatement(deleteRigheQuery)) {
                psDelete.setInt(1, idCarrello);
                psDelete.executeUpdate();
            }

            // 3. Inserisci le nuove righe attuali
            if (carrello.getRighe() != null && !carrello.getRighe().isEmpty()) {
                String insertRigaQuery = "INSERT INTO riga_carrello (id_carrello, id_prodotto, quantita) VALUES (?, ?, ?)";
                try (PreparedStatement psInsertRiga = con.prepareStatement(insertRigaQuery)) {
                    for (RigaCarrello riga : carrello.getRighe()) {
                        psInsertRiga.setInt(1, idCarrello);
                        psInsertRiga.setInt(2, riga.getProdotto().getId());
                        psInsertRiga.setInt(3, riga.getQuantita());
                        psInsertRiga.addBatch();
                    }
                    psInsertRiga.executeBatch();
                }
            }

            con.commit();
        } catch (SQLException | NamingException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Svuota il carrello persistito su database (invocato dopo il completamento del checkout).
     */
    public synchronized void doDeleteByUtente(int idUtente) throws SQLException, NamingException {
        String query = "DELETE rc FROM riga_carrello rc "
                     + "JOIN carrello c ON rc.id_carrello = c.id "
                     + "WHERE c.id_utente = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // METODI HELPER PRIVATI
    // =========================================================================

    private List<RigaCarrello> doRetrieveRigheByCarrello(int idCarrello) throws SQLException, NamingException {
        List<RigaCarrello> righe = new ArrayList<>();
        String query = "SELECT rc.id, rc.id_carrello, rc.quantita, "
                     + "p.id AS prod_id, p.nome AS prod_nome, p.descrizione AS prod_desc, "
                     + "p.prezzo AS prod_prezzo, p.quantita_disponibile AS prod_quantita, "
                     + "p.immagine AS prod_img, p.cancellato AS prod_canc, "
                     + "c.id AS cat_id, c.nome AS cat_nome, "
                     + "b.id AS brand_id, b.nome AS brand_nome "
                     + "FROM riga_carrello rc "
                     + "JOIN prodotto p ON rc.id_prodotto = p.id "
                     + "JOIN categoria c ON p.id_categoria = c.id "
                     + "JOIN brand b ON p.id_brand = b.id "
                     + "WHERE rc.id_carrello = ? AND p.cancellato = FALSE";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idCarrello);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RigaCarrello rc = new RigaCarrello();
                    rc.setId(rs.getInt("id"));
                    rc.setIdCarrello(rs.getInt("id_carrello"));
                    rc.setQuantita(rs.getInt("quantita"));

                    Prodotto p = new Prodotto();
                    p.setId(rs.getInt("prod_id"));
                    p.setNome(rs.getString("prod_nome"));
                    p.setDescrizione(rs.getString("prod_desc"));
                    p.setPrezzo(rs.getDouble("prod_prezzo"));
                    p.setQuantitaDisponibile(rs.getInt("prod_quantita"));
                    p.setImmagine(rs.getString("prod_img"));
                    p.setCancellato(rs.getBoolean("prod_canc"));

                    Categoria cat = new Categoria(rs.getInt("cat_id"), rs.getString("cat_nome"));
                    Brand brand = new Brand(rs.getInt("brand_id"), rs.getString("brand_nome"));
                    p.setCategoria(cat);
                    p.setBrand(brand);

                    rc.setProdotto(p);
                    righe.add(rc);
                }
            }
        }
        return righe;
    }
}
