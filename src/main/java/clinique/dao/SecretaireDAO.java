package clinique.dao;

import clinique.model.Secretaire;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecretaireDAO {

    public List<Secretaire> findAll() {
        List<Secretaire> liste = new ArrayList<>();
        String sql = "SELECT u.* FROM Utilisateur u " +
                     "JOIN Secretaire s ON u.idUtilisateur = s.idUtilisateur " +
                     "ORDER BY u.nomUtilisateur";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public Secretaire findById(int id) {
        String sql = "SELECT u.* FROM Utilisateur u " +
                     "JOIN Secretaire s ON u.idUtilisateur = s.idUtilisateur " +
                     "WHERE u.idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(String login, String motDePasse, String nom, String prenom,
                          String telephone, String email) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sql1 = "INSERT INTO Utilisateur " +
                          "(login, motDePasse, role, actif, nomUtilisateur, prenomUtilisateur, " +
                          "telephoneUtilisateur, emailUtilisateur) " +
                          "VALUES (?, SHA2(?,256), 'SECRETAIRE', TRUE, ?, ?, ?, ?)";
            PreparedStatement ps1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, login);
            ps1.setString(2, motDePasse);
            ps1.setString(3, nom);
            ps1.setString(4, prenom);
            ps1.setString(5, telephone);
            ps1.setString(6, email);
            ps1.executeUpdate();

            ResultSet keys = ps1.getGeneratedKeys();
            int newId = 0;
            if (keys.next()) newId = keys.getInt(1);

            String sql2 = "INSERT INTO Secretaire (idUtilisateur) VALUES (?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, newId);
            ps2.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean update(int idUtilisateur, String login, String nom, String prenom,
                          String telephone, String email) {
        String sql = "UPDATE Utilisateur SET login=?, nomUtilisateur=?, prenomUtilisateur=?, " +
                     "telephoneUtilisateur=?, emailUtilisateur=? WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, nom);
            ps.setString(3, prenom);
            ps.setString(4, telephone);
            ps.setString(5, email);
            ps.setInt(6, idUtilisateur);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int idUtilisateur) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM Secretaire WHERE idUtilisateur=?");
            ps1.setInt(1, idUtilisateur);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM Utilisateur WHERE idUtilisateur=?");
            ps2.setInt(1, idUtilisateur);
            ps2.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean toggleActif(int idUtilisateur) {
        String sql = "UPDATE Utilisateur SET actif = NOT actif WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Secretaire map(ResultSet rs) throws SQLException {
        Secretaire s = new Secretaire();
        s.setIdUtilisateur(rs.getInt("idUtilisateur"));
        s.setLogin(rs.getString("login"));
        s.setRole(rs.getString("role"));
        s.setActif(rs.getBoolean("actif"));
        s.setNomUtilisateur(rs.getString("nomUtilisateur"));
        s.setPrenomUtilisateur(rs.getString("prenomUtilisateur"));
        s.setTelephoneUtilisateur(rs.getString("telephoneUtilisateur"));
        s.setEmailUtilisateur(rs.getString("emailUtilisateur"));
        return s;
    }
}