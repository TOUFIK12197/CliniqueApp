package clinique.dao;

import clinique.model.Medecin;
import clinique.model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    public List<Medecin> findAll() {
        List<Medecin> liste = new ArrayList<>();
        String sql = "SELECT u.*, m.numOrdre FROM Utilisateur u " +
                     "JOIN Medecin m ON u.idUtilisateur = m.idUtilisateur " +
                     "ORDER BY u.nomUtilisateur";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public Medecin findById(int id) {
        String sql = "SELECT u.*, m.numOrdre FROM Utilisateur u " +
                     "JOIN Medecin m ON u.idUtilisateur = m.idUtilisateur " +
                     "WHERE u.idUtilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Créer un médecin complet (Utilisateur + Medecin + Specialite)
    public boolean insert(String login, String motDePasse, String nom, String prenom,
                          String telephone, String email, String numOrdre, int idSpecialite) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insérer dans Utilisateur
            String sql1 = "INSERT INTO Utilisateur " +
                          "(login, motDePasse, role, actif, nomUtilisateur, prenomUtilisateur, " +
                          "telephoneUtilisateur, emailUtilisateur) " +
                          "VALUES (?, SHA2(?,256), 'MEDECIN', TRUE, ?, ?, ?, ?)";
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

            // 2. Insérer dans Medecin
            String sql2 = "INSERT INTO Medecin (idUtilisateur, numOrdre) VALUES (?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, newId);
            ps2.setString(2, numOrdre);
            ps2.executeUpdate();

            // 3. Associer la spécialité
            if (idSpecialite > 0) {
                String sql3 = "INSERT INTO possede (idUtilisateur, idspecialite) VALUES (?, ?)";
                PreparedStatement ps3 = conn.prepareStatement(sql3);
                ps3.setInt(1, newId);
                ps3.setInt(2, idSpecialite);
                ps3.executeUpdate();
            }

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

    // Modifier un médecin
    public boolean update(int idUtilisateur, String login, String nom, String prenom,
                          String telephone, String email, String numOrdre, int idSpecialite) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Mettre à jour Utilisateur
            String sql1 = "UPDATE Utilisateur SET login=?, nomUtilisateur=?, prenomUtilisateur=?, " +
                          "telephoneUtilisateur=?, emailUtilisateur=? WHERE idUtilisateur=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, login);
            ps1.setString(2, nom);
            ps1.setString(3, prenom);
            ps1.setString(4, telephone);
            ps1.setString(5, email);
            ps1.setInt(6, idUtilisateur);
            ps1.executeUpdate();

            // 2. Mettre à jour numOrdre
            String sql2 = "UPDATE Medecin SET numOrdre=? WHERE idUtilisateur=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, numOrdre);
            ps2.setInt(2, idUtilisateur);
            ps2.executeUpdate();

            // 3. Mettre à jour spécialité
            String sqlDel = "DELETE FROM possede WHERE idUtilisateur=?";
            PreparedStatement psDel = conn.prepareStatement(sqlDel);
            psDel.setInt(1, idUtilisateur);
            psDel.executeUpdate();

            if (idSpecialite > 0) {
                String sql3 = "INSERT INTO possede (idUtilisateur, idspecialite) VALUES (?, ?)";
                PreparedStatement ps3 = conn.prepareStatement(sql3);
                ps3.setInt(1, idUtilisateur);
                ps3.setInt(2, idSpecialite);
                ps3.executeUpdate();
            }

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

    // Supprimer un médecin
    public boolean delete(int idUtilisateur) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Supprimer dans possede
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM possede WHERE idUtilisateur=?");
            ps1.setInt(1, idUtilisateur);
            ps1.executeUpdate();

            // Supprimer dans Medecin
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM Medecin WHERE idUtilisateur=?");
            ps2.setInt(1, idUtilisateur);
            ps2.executeUpdate();

            // Supprimer dans Utilisateur
            PreparedStatement ps3 = conn.prepareStatement("DELETE FROM Utilisateur WHERE idUtilisateur=?");
            ps3.setInt(1, idUtilisateur);
            ps3.executeUpdate();

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

    // Activer / Désactiver
    public boolean toggleActif(int idUtilisateur) {
        String sql = "UPDATE Utilisateur SET actif = NOT actif WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Récupérer la spécialité d'un médecin
    public int getSpecialiteId(int idUtilisateur) {
        String sql = "SELECT idspecialite FROM possede WHERE idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("idspecialite");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // Vérifier disponibilité
    public boolean isMedecinDisponible(int idMedecin, Timestamp dateHeure) {
        String sql = "SELECT COUNT(*) FROM prend pr JOIN RendezVous r ON pr.idRdv=r.idRdv " +
                     "WHERE pr.idUtilisateur=? AND r.dateHeure=? AND r.statut NOT IN ('ANNULE','TERMINE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedecin);
            ps.setTimestamp(2, dateHeure);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Medecin map(ResultSet rs) throws SQLException {
        Medecin m = new Medecin();
        m.setIdUtilisateur(rs.getInt("idUtilisateur"));
        m.setLogin(rs.getString("login"));
        m.setRole(rs.getString("role"));
        m.setActif(rs.getBoolean("actif"));
        m.setNomUtilisateur(rs.getString("nomUtilisateur"));
        m.setPrenomUtilisateur(rs.getString("prenomUtilisateur"));
        m.setTelephoneUtilisateur(rs.getString("telephoneUtilisateur"));
        m.setEmailUtilisateur(rs.getString("emailUtilisateur"));
        m.setNumOrdre(rs.getString("numOrdre"));
        return m;
    }
}