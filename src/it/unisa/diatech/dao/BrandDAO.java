package it.unisa.diatech.dao;

import it.unisa.diatech.model.Brand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

public class BrandDAO {

    private static final String TABLE_NAME = "brand";

    
    public List<Brand> doRetrieveAll() throws SQLException, NamingException {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT id, nome FROM " + TABLE_NAME + " ORDER BY nome ASC";

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Brand b = new Brand();
                b.setId(rs.getInt("id"));
                b.setNome(rs.getString("nome"));
                brands.add(b);
            }
        }
        return brands;
    }

    
    public Brand doRetrieveByKey(int id) throws SQLException, NamingException {
        String query = "SELECT id, nome FROM " + TABLE_NAME + " WHERE id = ?";
        Brand b = null;

        try (Connection con = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    b = new Brand();
                    b.setId(rs.getInt("id"));
                    b.setNome(rs.getString("nome"));
                }
            }
        }
        return b;
    }
}
