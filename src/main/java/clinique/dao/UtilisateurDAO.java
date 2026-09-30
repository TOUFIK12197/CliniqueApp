package clinique.dao;

import clinique.model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public Utilisateur authentifier(String login, String motDePasse) {
        String sql = "SELECT * FROM Utilisateur WHERE login=? AND motDePasse=SHA2(?,256) AND actif=TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Utilisateur> findAll() {
        List<Utilisateur> liste = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Utilisateur")) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM Utilisateur WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(Utilisateur u) {
        String sql = "INSERT INTO Utilisateur (login,motDePasse,role,actif,nomUtilisateur,prenomUtilisateur,telephoneUtilisateur,emailUtilisateur) "
                   + "VALUES (?,SHA2(?,256),?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getLogin());
            ps.setString(2, u.getMotDePasse());
            ps.setString(3, u.getRole());
            ps.setBoolean(4, u.isActif());
            ps.setString(5, u.getNomUtilisateur());
            ps.setString(6, u.getPrenomUtilisateur());
            ps.setString(7, u.getTelephoneUtilisateur());
            ps.setString(8, u.getEmailUtilisateur());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Utilisateur u) {
        String sql = "UPDATE Utilisateur SET login=?,role=?,actif=?,nomUtilisateur=?,prenomUtilisateur=?,telephoneUtilisateur=?,emailUtilisateur=? WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getLogin());
            ps.setString(2, u.getRole());
            ps.setBoolean(3, u.isActif());
            ps.setString(4, u.getNomUtilisateur());
            ps.setString(5, u.getPrenomUtilisateur());
            ps.setString(6, u.getTelephoneUtilisateur());
            ps.setString(7, u.getEmailUtilisateur());
            ps.setInt(8, u.getIdUtilisateur());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Utilisateur WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Utilisateur map(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(rs.getInt("idUtilisateur"));
        u.setLogin(rs.getString("login"));
        u.setMotDePasse(rs.getString("motDePasse"));
        u.setRole(rs.getString("role"));
        u.setActif(rs.getBoolean("actif"));
        u.setNomUtilisateur(rs.getString("nomUtilisateur"));
        u.setPrenomUtilisateur(rs.getString("prenomUtilisateur"));
        u.setTelephoneUtilisateur(rs.getString("telephoneUtilisateur"));
        u.setEmailUtilisateur(rs.getString("emailUtilisateur"));
        return u;
    }
}