package clinique.dao;

import clinique.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    public boolean insert(Consultation c, int idMedecin, int idPatient, int idRdv) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sql1 = "INSERT INTO Consultation (DateConsult,diagnostic,tension,notes,prescription,poids) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps1.setDate(1, c.getDateConsult());
            ps1.setString(2, c.getDiagnostic());
            ps1.setString(3, c.getTension());
            ps1.setString(4, c.getNotes());
            ps1.setString(5, c.getPrescription());
            ps1.setBigDecimal(6, c.getPoids());
            ps1.executeUpdate();
            ResultSet keys = ps1.getGeneratedKeys();
            int idConsultation = 0;
            if (keys.next()) idConsultation = keys.getInt(1);

            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO realise (idUtilisateur,idConsultation) VALUES (?,?)");
            ps2.setInt(1, idMedecin); ps2.setInt(2, idConsultation);
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement("INSERT INTO concerne (idUtilisateur,idPatient,idConsultation) VALUES (?,?,?)");
            ps3.setInt(1, idMedecin); ps3.setInt(2, idPatient); ps3.setInt(3, idConsultation);
            ps3.executeUpdate();

            PreparedStatement ps4 = conn.prepareStatement("UPDATE RendezVous SET idConsultation=?, statut='TERMINE' WHERE idRdv=?");
            ps4.setInt(1, idConsultation); ps4.setInt(2, idRdv);
            ps4.executeUpdate();

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

    public List<Consultation> findByPatient(int idPatient) {
        List<Consultation> liste = new ArrayList<>();
        String sql = "SELECT c.*, u.nomUtilisateur, u.prenomUtilisateur FROM Consultation c "
                   + "JOIN concerne con ON c.idConsultation=con.idConsultation "
                   + "JOIN Utilisateur u ON con.idUtilisateur=u.idUtilisateur "
                   + "WHERE con.idPatient=? ORDER BY c.DateConsult DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPatient);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    private Consultation map(ResultSet rs) throws SQLException {
        Consultation c = new Consultation();
        c.setIdConsultation(rs.getInt("idConsultation"));
        c.setDateConsult(rs.getDate("DateConsult"));
        c.setDiagnostic(rs.getString("diagnostic"));
        c.setTension(rs.getString("tension"));
        c.setNotes(rs.getString("notes"));
        c.setPrescription(rs.getString("prescription"));
        c.setPoids(rs.getBigDecimal("poids"));
        Medecin m = new Medecin();
        m.setNomUtilisateur(rs.getString("nomUtilisateur"));
        m.setPrenomUtilisateur(rs.getString("prenomUtilisateur"));
        c.setMedecin(m);
        return c;
    }
}