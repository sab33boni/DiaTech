package it.unisa.diatech.dao;

import it.unisa.diatech.model.Brand;
import it.unisa.diatech.model.Categoria;
import it.unisa.diatech.model.Prodotto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class ProdottoDAO {

    private static final String BASE_QUERY =
            "SELECT p.id, p.nome, p.descrizione, p.prezzo, p.quantita_disponibile, p.immagine, p.cancellato, "
            + "p.id_categoria, c.nome AS nome_categoria, "
            + "p.id_brand, b.nome AS nome_brand "
            + "FROM prodotto p "
            + "JOIN categoria c ON p.id_categoria = c.id "
            + "JOIN brand b ON p.id_brand = b.id ";

    
    public List<Prodotto> doRetrieveAll(String orderBy) throws SQLException, NamingException {
        List<Prodotto> prodotti = new ArrayList<>();
        String orderClause = sanitizeOrderBy(orderBy);
        String query = BASE_QUERY + "WHERE p.cancellato = FALSE ORDER BY " + orderClause;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                prodotti.add(mapProdotto(rs));
            }
        }
        return prodotti;
    }

    
    public List<Prodotto> doRetrieveAllAdmin() throws SQLException, NamingException {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = BASE_QUERY + "ORDER BY p.id ASC";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                prodotti.add(mapProdotto(rs));
            }
        }
        return prodotti;
    }

    
    public Prodotto doRetrieveByKey(int id) throws SQLException, NamingException {
        String query = BASE_QUERY + "WHERE p.id = ?";
        Prodotto p = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapProdotto(rs);
                }
            }
        }
        return p;
    }

    
    public List<Prodotto> doRetrieveByFiltri(Integer idCategoria, Integer idBrand, Double minPrezzo, Double maxPrezzo, String orderBy)
            throws SQLException, NamingException {
        List<Prodotto> prodotti = new ArrayList<>();
        StringBuilder query = new StringBuilder(BASE_QUERY).append("WHERE p.cancellato = FALSE ");
        List<Object> params = new ArrayList<>();

        if (idCategoria != null && idCategoria > 0) {
            query.append("AND p.id_categoria = ? ");
            params.add(idCategoria);
        }
        if (idBrand != null && idBrand > 0) {
            query.append("AND p.id_brand = ? ");
            params.add(idBrand);
        }
        if (minPrezzo != null && minPrezzo >= 0) {
            query.append("AND p.prezzo >= ? ");
            params.add(minPrezzo);
        }
        if (maxPrezzo != null && maxPrezzo >= 0) {
            query.append("AND p.prezzo <= ? ");
            params.add(maxPrezzo);
        }

        query.append("ORDER BY ").append(sanitizeOrderBy(orderBy));

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(mapProdotto(rs));
                }
            }
        }
        return prodotti;
    }

    
    public List<Prodotto> doRetrieveBySearch(String keyword) throws SQLException, NamingException {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = BASE_QUERY + "WHERE p.cancellato = FALSE AND (LOWER(p.nome) LIKE ? OR LOWER(p.descrizione) LIKE ?) ORDER BY p.nome ASC";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            String pattern = "%" + (keyword != null ? keyword.trim().toLowerCase() : "") + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(mapProdotto(rs));
                }
            }
        }
        return prodotti;
    }

    
    public List<Prodotto> doRetrieveCompatibili(int idProdotto) throws SQLException, NamingException {
        List<Prodotto> compatibili = new ArrayList<>();
        String query = BASE_QUERY
                + "JOIN prodotto_compatibile pc ON p.id = pc.id_prodotto_2 "
                + "WHERE pc.id_prodotto_1 = ? AND p.cancellato = FALSE";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idProdotto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    compatibili.add(mapProdotto(rs));
                }
            }
        }
        return compatibili;
    }

    
    public synchronized int doSave(Prodotto prodotto) throws SQLException, NamingException {
        String query = "INSERT INTO prodotto (nome, descrizione, prezzo, quantita_disponibile, immagine, id_categoria, id_brand, cancellato) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getDescrizione());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setInt(4, prodotto.getQuantitaDisponibile());
            ps.setString(5, prodotto.getImmagine() != null ? prodotto.getImmagine() : "default.png");
            ps.setInt(6, prodotto.getCategoria().getId());
            ps.setInt(7, prodotto.getBrand().getId());
            ps.setBoolean(8, prodotto.isCancellato());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    prodotto.setId(generatedId);
                }
            }
        }
        return generatedId;
    }

    
    public synchronized boolean doUpdate(Prodotto prodotto) throws SQLException, NamingException {
        String query = "UPDATE prodotto SET nome = ?, descrizione = ?, prezzo = ?, quantita_disponibile = ?, "
                + "immagine = ?, id_categoria = ?, id_brand = ?, cancellato = ? WHERE id = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getDescrizione());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setInt(4, prodotto.getQuantitaDisponibile());
            ps.setString(5, prodotto.getImmagine());
            ps.setInt(6, prodotto.getCategoria().getId());
            ps.setInt(7, prodotto.getBrand().getId());
            ps.setBoolean(8, prodotto.isCancellato());
            ps.setInt(9, prodotto.getId());

            return ps.executeUpdate() > 0;
        }
    }

    
    public synchronized boolean doDelete(int id) throws SQLException, NamingException {
        String query = "UPDATE prodotto SET cancellato = TRUE WHERE id = ?";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Prodotto mapProdotto(ResultSet rs) throws SQLException {
        Prodotto p = new Prodotto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescrizione(rs.getString("descrizione"));
        p.setPrezzo(rs.getDouble("prezzo"));
        p.setQuantitaDisponibile(rs.getInt("quantita_disponibile"));
        p.setImmagine(rs.getString("immagine"));
        p.setCancellato(rs.getBoolean("cancellato"));

        Categoria c = new Categoria();
        c.setId(rs.getInt("id_categoria"));
        c.setNome(rs.getString("nome_categoria"));
        p.setCategoria(c);

        Brand b = new Brand();
        b.setId(rs.getInt("id_brand"));
        b.setNome(rs.getString("nome_brand"));
        p.setBrand(b);

        return p;
    }

    private String sanitizeOrderBy(String orderBy) {
        if ("prezzo_asc".equalsIgnoreCase(orderBy)) return "p.prezzo ASC";
        if ("prezzo_desc".equalsIgnoreCase(orderBy)) return "p.prezzo DESC";
        if ("nome_asc".equalsIgnoreCase(orderBy)) return "p.nome ASC";
        if ("nome_desc".equalsIgnoreCase(orderBy)) return "p.nome DESC";
        return "p.id ASC";
    }
}
