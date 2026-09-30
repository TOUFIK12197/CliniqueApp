package clinique.dao;

import clinique.model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Tous les patients (pour Admin et Secrétaire)
    public List<Patient> findAll() {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT * FROM Patient ORDER BY nomPatient, prenomPatient";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // Patients d'un médecin spécifique (via table prend)
    public List<Patient> findByMedecin(int idMedecin) {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM Patient p " +
                     "JOIN prend pr ON p.idPatient = pr.idPatient " +
                     "WHERE pr.idUtilisateur = ? " +
                     "ORDER BY p.nomPatient, p.prenomPatient";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedecin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // Recherche parmi les patients d'un médecin
    public List<Patient> rechercherByMedecin(String terme, int idMedecin) {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM Patient p " +
                     "JOIN prend pr ON p.idPatient = pr.idPatient " +
                     "WHERE pr.idUtilisateur = ? " +
                     "AND (p.nomPatient LIKE ? OR p.prenomPatient LIKE ? OR p.emailPatient LIKE ?) " +
                     "ORDER BY p.nomPatient, p.prenomPatient";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String t = "%" + terme + "%";
            ps.setInt(1, idMedecin);
            ps.setString(2, t);
            ps.setString(3, t);
            ps.setString(4, t);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public Patient findById(int id) {
        String sql = "SELECT * FROM Patient WHERE idPatient=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Patient> rechercher(String terme) {
        List<Patient> liste = new ArrayList<>();
        String sql = "SELECT * FROM Patient " +
                     "WHERE nomPatient LIKE ? OR prenomPatient LIKE ? OR emailPatient LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String t = "%" + terme + "%";
            ps.setString(1, t);
            ps.setString(2, t);
            ps.setString(3, t);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public boolean insert(Patient p) {
        String sql = "INSERT INTO Patient " +
                     "(nomPatient, prenomPatient, dateNaissance, telephonePatient, " +
                     "emailPatient, adresse, groupeSanguin) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNomPatient());
            ps.setString(2, p.getPrenomPatient());
            ps.setDate(3, p.getDateNaissance());
            ps.setString(4, p.getTelephonePatient());
            ps.setString(5, p.getEmailPatient());
            ps.setString(6, p.getAdresse());
            ps.setString(7, p.getGroupeSanguin());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Patient p) {
        String sql = "UPDATE Patient SET nomPatient=?, prenomPatient=?, dateNaissance=?, " +
                     "telephonePatient=?, emailPatient=?, adresse=?, groupeSanguin=? " +
                     "WHERE idPatient=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNomPatient());
            ps.setString(2, p.getPrenomPatient());
            ps.setDate(3, p.getDateNaissance());
            ps.setString(4, p.getTelephonePatient());
            ps.setString(5, p.getEmailPatient());
            ps.setString(6, p.getAdresse());
            ps.setString(7, p.getGroupeSanguin());
            ps.setInt(8, p.getIdPatient());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        if (hasConsultation(id)) return false;
        String sql = "DELETE FROM Patient WHERE idPatient=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean hasConsultation(int idPatient) {
        String sql = "SELECT COUNT(*) FROM concerne WHERE idPatient=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return true;
    }

    // Vérifier si un patient appartient à un médecin
    public boolean isPatientDuMedecin(int idPatient, int idMedecin) {
        String sql = "SELECT COUNT(*) FROM prend " +
                     "WHERE idPatient=? AND idUtilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            ps.setInt(2, idMedecin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Patient map(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setIdPatient(rs.getInt("idPatient"));
        p.setNomPatient(rs.getString("nomPatient"));
        p.setPrenomPatient(rs.getString("prenomPatient"));
        p.setDateNaissance(rs.getDate("dateNaissance"));
        p.setTelephonePatient(rs.getString("telephonePatient"));
        p.setEmailPatient(rs.getString("emailPatient"));
        p.setAdresse(rs.getString("adresse"));
        p.setGroupeSanguin(rs.getString("groupeSanguin"));
        return p;
    }
}