package it.unisa.diatech.dao;

import it.unisa.diatech.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class CategoriaDAO {

    private static final String TABLE_NAME = "categoria";

    
    public List<Categoria> doRetrieveAll() throws SQLException, NamingException {
        List<Categoria> categorie = new ArrayList<>();
        String query = "SELECT id, nome FROM " + TABLE_NAME + " ORDER BY nome ASC";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                categorie.add(c);
            }
        }
        return categorie;
    }

    
    public Categoria doRetrieveByKey(int id) throws SQLException, NamingException {
        String query = "SELECT id, nome FROM " + TABLE_NAME + " WHERE id = ?";
        Categoria c = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Categoria();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                }
            }
        }
        return c;
    }
}
