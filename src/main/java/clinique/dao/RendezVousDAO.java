package clinique.dao;

import clinique.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {

    public List<RendezVous> findAll() {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT r.*, p.nomPatient, p.prenomPatient, p.idPatient, "
                   + "u.nomUtilisateur, u.prenomUtilisateur, pr.idUtilisateur AS idMedecin "
                   + "FROM RendezVous r "
                   + "JOIN prend pr ON r.idRdv=pr.idRdv "
                   + "JOIN Patient p ON pr.idPatient=p.idPatient "
                   + "JOIN Utilisateur u ON pr.idUtilisateur=u.idUtilisateur "
                   + "ORDER BY r.dateHeure DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public List<RendezVous> findByMedecinAndDate(int idMedecin, String date) {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT r.*, p.nomPatient, p.prenomPatient, p.idPatient, "
                   + "u.nomUtilisateur, u.prenomUtilisateur, pr.idUtilisateur AS idMedecin "
                   + "FROM RendezVous r "
                   + "JOIN prend pr ON r.idRdv=pr.idRdv "
                   + "JOIN Patient p ON pr.idPatient=p.idPatient "
                   + "JOIN Utilisateur u ON pr.idUtilisateur=u.idUtilisateur "
                   + "WHERE pr.idUtilisateur=? AND DATE(r.dateHeure)=? "
                   + "ORDER BY r.dateHeure";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedecin);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public RendezVous findById(int id) {
        String sql = "SELECT r.*, p.nomPatient, p.prenomPatient, p.idPatient, "
                   + "u.nomUtilisateur, u.prenomUtilisateur, pr.idUtilisateur AS idMedecin "
                   + "FROM RendezVous r "
                   + "JOIN prend pr ON r.idRdv=pr.idRdv "
                   + "JOIN Patient p ON pr.idPatient=p.idPatient "
                   + "JOIN Utilisateur u ON pr.idUtilisateur=u.idUtilisateur "
                   + "WHERE r.idRdv=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(RendezVous rdv, int idMedecin, int idPatient) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Vérifier disponibilité
            MedecinDAO medDAO = new MedecinDAO();
            if (!medDAO.isMedecinDisponible(idMedecin, rdv.getDateHeure())) {
                conn.rollback();
                return false;
            }

            // Insérer RendezVous
            String sql1 = "INSERT INTO RendezVous (dateHeure, motif, statut) VALUES (?, ?, 'EN_ATTENTE')";
            PreparedStatement ps1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps1.setTimestamp(1, rdv.getDateHeure());
            ps1.setString(2, rdv.getMotif());
            ps1.executeUpdate();
            ResultSet keys = ps1.getGeneratedKeys();
            int idRdv = 0;
            if (keys.next()) idRdv = keys.getInt(1);

            // Insérer dans prend
            String sql2 = "INSERT INTO prend (idUtilisateur, idPatient, idRdv) VALUES (?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, idMedecin);
            ps2.setInt(2, idPatient);
            ps2.setInt(3, idRdv);
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

    public boolean updateStatut(int idRdv, String statut) {
        RendezVous rdv = findById(idRdv);
        if (rdv != null && "ANNULE".equals(rdv.getStatut())) return false;
        String sql = "UPDATE RendezVous SET statut=? WHERE idRdv=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, idRdv);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(RendezVous rdv) {
        RendezVous existing = findById(rdv.getIdRdv());
        if (existing != null && "ANNULE".equals(existing.getStatut())) return false;
        String sql = "UPDATE RendezVous SET dateHeure=?, motif=?, statut=? WHERE idRdv=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, rdv.getDateHeure());
            ps.setString(2, rdv.getMotif());
            ps.setString(3, rdv.getStatut());
            ps.setInt(4, rdv.getIdRdv());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private RendezVous map(ResultSet rs) throws SQLException {
        RendezVous rdv = new RendezVous();
        rdv.setIdRdv(rs.getInt("idRdv"));
        rdv.setDateHeure(rs.getTimestamp("dateHeure"));
        rdv.setMotif(rs.getString("motif"));
        rdv.setStatut(rs.getString("statut"));
        int idC = rs.getInt("idConsultation");
        if (!rs.wasNull()) rdv.setIdConsultation(idC);

        Patient p = new Patient();
        p.setIdPatient(rs.getInt("idPatient"));
        p.setNomPatient(rs.getString("nomPatient"));
        p.setPrenomPatient(rs.getString("prenomPatient"));
        rdv.setPatient(p);

        Medecin m = new Medecin();
        m.setIdUtilisateur(rs.getInt("idMedecin"));
        m.setNomUtilisateur(rs.getString("nomUtilisateur"));
        m.setPrenomUtilisateur(rs.getString("prenomUtilisateur"));
        rdv.setMedecin(m);
        return rdv;
    }

//Vérifier si un RDV appartient à un médecin
public boolean isRdvDuMedecin(int idRdv, int idMedecin) {
 String sql = "SELECT COUNT(*) FROM prend WHERE idRdv=? AND idUtilisateur=?";
 try (Connection conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql)) {
     ps.setInt(1, idRdv);
     ps.setInt(2, idMedecin);
     ResultSet rs = ps.executeQuery();
     if (rs.next()) return rs.getInt(1) > 0;
 } catch (SQLException e) { e.printStackTrace(); }
 return false;
}
}