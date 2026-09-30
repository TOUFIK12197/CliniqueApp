package clinique.dao;

import clinique.model.Specialite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialiteDAO {

    public List<Specialite> findAll() {
        List<Specialite> liste = new ArrayList<>();
        String sql = "SELECT * FROM specialite ORDER BY libelle";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Specialite(
                    rs.getInt("idspecialite"),
                    rs.getString("libelle"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public Specialite findById(int id) {
        String sql = "SELECT * FROM specialite WHERE idspecialite=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Specialite(
                    rs.getInt("idspecialite"),
                    rs.getString("libelle"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(String libelle, String description) {
        String sql = "INSERT INTO specialite (libelle, description) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libelle);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(int id, String libelle, String description) {
        String sql = "UPDATE specialite SET libelle=?, description=? WHERE idspecialite=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libelle);
            ps.setString(2, description);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM specialite WHERE idspecialite=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}