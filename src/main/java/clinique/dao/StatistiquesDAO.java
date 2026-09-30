package clinique.dao;

import clinique.model.Statistiques;
import java.sql.*;
import java.util.*;

public class StatistiquesDAO {

    public Statistiques getStatistiquesGlobales() {
        Statistiques stats = new Statistiques();
        try (Connection conn = DBConnection.getConnection()) {

            ResultSet r1 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM Patient");
            if (r1.next()) stats.setNbPatient(r1.getInt(1));

            ResultSet r2 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM RendezVous");
            if (r2.next()) stats.setNbRendezVous(r2.getInt(1));

            ResultSet r3 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM RendezVous WHERE statut='ANNULE'");
            if (r3.next()) stats.setNbAnnulations(r3.getInt(1));

            ResultSet r4 = conn.createStatement()
                .executeQuery("SELECT COUNT(*) FROM Consultation");
            if (r4.next()) stats.setNbconsultation(r4.getInt(1));

        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    // Consultations par médecin (getNbConsultationsParMedecin du diagramme)
    public Map<String, Integer> getNbConsultationsParMedecin() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT CONCAT(u.prenomUtilisateur,' ',u.nomUtilisateur) AS nomMedecin, " +
                     "COUNT(r.idConsultation) AS nb " +
                     "FROM Utilisateur u " +
                     "JOIN Medecin m ON u.idUtilisateur = m.idUtilisateur " +
                     "LEFT JOIN realise r ON m.idUtilisateur = r.idUtilisateur " +
                     "GROUP BY u.idUtilisateur, u.prenomUtilisateur, u.nomUtilisateur " +
                     "ORDER BY nb DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("nomMedecin"), rs.getInt("nb"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    // Rendez-vous par statut
    public Map<String, Integer> getRdvParStatut() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT statut, COUNT(*) AS nb FROM RendezVous GROUP BY statut";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("statut"), rs.getInt("nb"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
}