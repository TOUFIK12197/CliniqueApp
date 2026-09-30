package clinique.dao;

import clinique.model.DocumentMedical;
import clinique.model.Patient;
import clinique.model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentMedicalDAO {

    // Insérer un document
    public boolean insert(DocumentMedical doc) {
        String sql = "INSERT INTO DocumentMedical " +
                     "(idPatient, idConsultation, idUtilisateur, typeDocument, " +
                     "nomFichier, nomOriginal, cheminFichier, tailleFichier, typeMime, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, doc.getIdPatient());
            if (doc.getIdConsultation() != null) {
                ps.setInt(2, doc.getIdConsultation());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, doc.getIdUtilisateur());
            ps.setString(4, doc.getTypeDocument());
            ps.setString(5, doc.getNomFichier());
            ps.setString(6, doc.getNomOriginal());
            ps.setString(7, doc.getCheminFichier());
            ps.setLong(8, doc.getTailleFichier());
            ps.setString(9, doc.getTypeMime());
            ps.setString(10, doc.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Tous les documents d'un patient
    public List<DocumentMedical> findByPatient(int idPatient) {
        List<DocumentMedical> liste = new ArrayList<>();
        String sql = "SELECT d.*, u.nomUtilisateur, u.prenomUtilisateur " +
                     "FROM DocumentMedical d " +
                     "JOIN Utilisateur u ON d.idUtilisateur = u.idUtilisateur " +
                     "WHERE d.idPatient = ? " +
                     "ORDER BY d.dateUpload DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // Documents d'une consultation
    public List<DocumentMedical> findByConsultation(int idConsultation) {
        List<DocumentMedical> liste = new ArrayList<>();
        String sql = "SELECT d.*, u.nomUtilisateur, u.prenomUtilisateur " +
                     "FROM DocumentMedical d " +
                     "JOIN Utilisateur u ON d.idUtilisateur = u.idUtilisateur " +
                     "WHERE d.idConsultation = ? " +
                     "ORDER BY d.dateUpload DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConsultation);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // Trouver par ID
    public DocumentMedical findById(int id) {
        String sql = "SELECT d.*, u.nomUtilisateur, u.prenomUtilisateur " +
                     "FROM DocumentMedical d " +
                     "JOIN Utilisateur u ON d.idUtilisateur = u.idUtilisateur " +
                     "WHERE d.idDocument = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Documents par type
    public List<DocumentMedical> findByPatientAndType(int idPatient, String type) {
        List<DocumentMedical> liste = new ArrayList<>();
        String sql = "SELECT d.*, u.nomUtilisateur, u.prenomUtilisateur " +
                     "FROM DocumentMedical d " +
                     "JOIN Utilisateur u ON d.idUtilisateur = u.idUtilisateur " +
                     "WHERE d.idPatient = ? AND d.typeDocument = ? " +
                     "ORDER BY d.dateUpload DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // Supprimer un document
    public boolean delete(int id) {
        String sql = "DELETE FROM DocumentMedical WHERE idDocument = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Compter les documents d'un patient
    public int countByPatient(int idPatient) {
        String sql = "SELECT COUNT(*) FROM DocumentMedical WHERE idPatient = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private DocumentMedical map(ResultSet rs) throws SQLException {
        DocumentMedical doc = new DocumentMedical();
        doc.setIdDocument(rs.getInt("idDocument"));
        doc.setIdPatient(rs.getInt("idPatient"));
        int idC = rs.getInt("idConsultation");
        if (!rs.wasNull()) doc.setIdConsultation(idC);
        doc.setIdUtilisateur(rs.getInt("idUtilisateur"));
        doc.setTypeDocument(rs.getString("typeDocument"));
        doc.setNomFichier(rs.getString("nomFichier"));
        doc.setNomOriginal(rs.getString("nomOriginal"));
        doc.setCheminFichier(rs.getString("cheminFichier"));
        doc.setTailleFichier(rs.getLong("tailleFichier"));
        doc.setTypeMime(rs.getString("typeMime"));
        doc.setDescription(rs.getString("description"));
        doc.setDateUpload(rs.getTimestamp("dateUpload"));

        Utilisateur u = new Utilisateur();
        u.setNomUtilisateur(rs.getString("nomUtilisateur"));
        u.setPrenomUtilisateur(rs.getString("prenomUtilisateur"));
        doc.setUploadeur(u);

        return doc;
    }
}