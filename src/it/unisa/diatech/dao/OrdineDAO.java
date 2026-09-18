package it.unisa.diatech.dao;

import it.unisa.diatech.model.Brand;
import it.unisa.diatech.model.Categoria;
import it.unisa.diatech.model.Ordine;
import it.unisa.diatech.model.Prodotto;
import it.unisa.diatech.model.RigaOrdine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class OrdineDAO {

    
    public synchronized int doSave(Ordine ordine) throws SQLException, NamingException {
        String insertOrdineQuery = "INSERT INTO ordine (id_utente, stato, totale, indirizzo_spedizione, citta, cap, metodo_pagamento) "
                                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertRigaQuery = "INSERT INTO riga_ordine (id_ordine, id_prodotto, quantita, prezzo_unitario) "
                               + "VALUES (?, ?, ?, ?)";
        String updateStockQuery = "UPDATE prodotto SET quantita_disponibile = quantita_disponibile - ? "
                                + "WHERE id = ? AND quantita_disponibile >= ?";
        String insertGaranziaQuery = "INSERT INTO garanzia (id_riga_ordine, data_scadenza, stato) "
                                   + "VALUES (?, ?, 'ATTIVA')";

        int idOrdineGenerato = -1;
        Connection con = null;

        try {
            con = DataSourceSingleton.getInstance().getConnection();
            con.setAutoCommit(false);
            try (PreparedStatement psOrdine = con.prepareStatement(insertOrdineQuery, Statement.RETURN_GENERATED_KEYS)) {
                psOrdine.setInt(1, ordine.getIdUtente());
                psOrdine.setString(2, ordine.getStato() != null ? ordine.getStato() : "IN_LAVORAZIONE");
                psOrdine.setDouble(3, ordine.getTotale());
                psOrdine.setString(4, ordine.getIndirizzoSpedizione());
                psOrdine.setString(5, ordine.getCitta());
                psOrdine.setString(6, ordine.getCap());
                psOrdine.setString(7, ordine.getMetodoPagamento());

                psOrdine.executeUpdate();

                try (ResultSet rsKeys = psOrdine.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idOrdineGenerato = rsKeys.getInt(1);
                        ordine.setId(idOrdineGenerato);
                    } else {
                        throw new SQLException("Errore: generazione ID ordine fallita.");
                    }
                }
            }
            try (PreparedStatement psRiga = con.prepareStatement(insertRigaQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psStock = con.prepareStatement(updateStockQuery);
                 PreparedStatement psGaranzia = con.prepareStatement(insertGaranziaQuery)) {

                Date dataScadenzaGaranzia = Date.valueOf(LocalDate.now().plusYears(2));

                for (RigaOrdine riga : ordine.getRighe()) {
                    psRiga.setInt(1, idOrdineGenerato);
                    psRiga.setInt(2, riga.getProdotto().getId());
                    psRiga.setInt(3, riga.getQuantita());
                    psRiga.setDouble(4, riga.getPrezzoUnitario());
                    psRiga.executeUpdate();

                    int idRigaGenerata = -1;
                    try (ResultSet rsRigaKeys = psRiga.getGeneratedKeys()) {
                        if (rsRigaKeys.next()) {
                            idRigaGenerata = rsRigaKeys.getInt(1);
                            riga.setId(idRigaGenerata);
                        }
                    }
                    psStock.setInt(1, riga.getQuantita());
                    psStock.setInt(2, riga.getProdotto().getId());
                    psStock.setInt(3, riga.getQuantita());
                    int rowsUpdated = psStock.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException("Scorte insufficienti per il prodotto: " + riga.getProdotto().getNome());
                    }
                    if (idRigaGenerata > 0) {
                        psGaranzia.setInt(1, idRigaGenerata);
                        psGaranzia.setDate(2, dataScadenzaGaranzia);
                        psGaranzia.executeUpdate();
                    }
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

        return idOrdineGenerato;
    }

    
    public Ordine doRetrieveByKey(int id) throws SQLException, NamingException {
        String query = "SELECT id, id_utente, data_ordine, stato, totale, indirizzo_spedizione, citta, cap, metodo_pagamento "
                     + "FROM ordine WHERE id = ?";
        Ordine ordine = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ordine = mapOrdine(rs);
                }
            }
        }

        if (ordine != null) {
            ordine.setRighe(doRetrieveRigheByOrdine(ordine.getId()));
        }

        return ordine;
    }

    
    public List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException, NamingException {
        List<Ordine> ordini = new ArrayList<>();
        String query = "SELECT id, id_utente, data_ordine, stato, totale, indirizzo_spedizione, citta, cap, metodo_pagamento "
                     + "FROM ordine WHERE id_utente = ? ORDER BY data_ordine DESC";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ordine o = mapOrdine(rs);
                    o.setRighe(doRetrieveRigheByOrdine(o.getId()));
                    ordini.add(o);
                }
            }
        }
        return ordini;
    }

    
    public List<Ordine> doRetrieveByFiltri(String startDate, String endDate, Integer idCliente) throws SQLException, NamingException {
        List<Ordine> ordini = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT id, id_utente, data_ordine, stato, totale, indirizzo_spedizione, citta, cap, metodo_pagamento FROM ordine WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (startDate != null && !startDate.trim().isEmpty()) {
            query.append("AND DATE(data_ordine) >= ? ");
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            query.append("AND DATE(data_ordine) <= ? ");
            params.add(Date.valueOf(endDate));
        }
        if (idCliente != null && idCliente > 0) {
            query.append("AND id_utente = ? ");
            params.add(idCliente);
        }

        query.append("ORDER BY data_ordine DESC");

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ordine o = mapOrdine(rs);
                    o.setRighe(doRetrieveRigheByOrdine(o.getId()));
                    ordini.add(o);
                }
            }
        }
        return ordini;
    }

    
    public synchronized boolean doUpdateStato(int idOrdine, String nuovoStato) throws SQLException, NamingException {
        String query = "UPDATE ordine SET stato = ? WHERE id = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nuovoStato);
            ps.setInt(2, idOrdine);

            return ps.executeUpdate() > 0;
        }
    }

    private List<RigaOrdine> doRetrieveRigheByOrdine(int idOrdine) throws SQLException, NamingException {
        List<RigaOrdine> righe = new ArrayList<>();
        String query = "SELECT ro.id, ro.id_ordine, ro.quantita, ro.prezzo_unitario, "
                     + "p.id AS prod_id, p.nome AS prod_nome, p.descrizione AS prod_desc, "
                     + "p.immagine AS prod_img, p.cancellato AS prod_canc, "
                     + "c.id AS cat_id, c.nome AS cat_nome, "
                     + "b.id AS brand_id, b.nome AS brand_nome "
                     + "FROM riga_ordine ro "
                     + "JOIN prodotto p ON ro.id_prodotto = p.id "
                     + "JOIN categoria c ON p.id_categoria = c.id "
                     + "JOIN brand b ON p.id_brand = b.id "
                     + "WHERE ro.id_ordine = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idOrdine);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RigaOrdine ro = new RigaOrdine();
                    ro.setId(rs.getInt("id"));
                    ro.setIdOrdine(rs.getInt("id_ordine"));
                    ro.setQuantita(rs.getInt("quantita"));
                    ro.setPrezzoUnitario(rs.getDouble("prezzo_unitario"));

                    Prodotto p = new Prodotto();
                    p.setId(rs.getInt("prod_id"));
                    p.setNome(rs.getString("prod_nome"));
                    p.setDescrizione(rs.getString("prod_desc"));
                    p.setPrezzo(ro.getPrezzoUnitario());
                    p.setImmagine(rs.getString("prod_img"));
                    p.setCancellato(rs.getBoolean("prod_canc"));

                    Categoria c = new Categoria(rs.getInt("cat_id"), rs.getString("cat_nome"));
                    Brand b = new Brand(rs.getInt("brand_id"), rs.getString("brand_nome"));
                    p.setCategoria(c);
                    p.setBrand(b);

                    ro.setProdotto(p);
                    righe.add(ro);
                }
            }
        }
        return righe;
    }

    private Ordine mapOrdine(ResultSet rs) throws SQLException {
        Ordine o = new Ordine();
        o.setId(rs.getInt("id"));
        o.setIdUtente(rs.getInt("id_utente"));
        o.setDataOrdine(rs.getString("data_ordine"));
        o.setStato(rs.getString("stato"));
        o.setTotale(rs.getDouble("totale"));
        o.setIndirizzoSpedizione(rs.getString("indirizzo_spedizione"));
        o.setCitta(rs.getString("citta"));
        o.setCap(rs.getString("cap"));
        o.setMetodoPagamento(rs.getString("metodo_pagamento"));
        return o;
    }
}
