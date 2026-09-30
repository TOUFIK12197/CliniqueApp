package clinique.util;

import clinique.model.Patient;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MassAssignmentProtector {

    private static final Set<String> GROUPES_SANGUINS = new HashSet<>(Arrays.asList(
        "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    ));

    private static final Set<String> STATUTS_RDV = new HashSet<>(Arrays.asList(
        "EN_ATTENTE", "CONFIRME", "ANNULE", "TERMINE"
    ));

    public static Patient buildPatient(HttpServletRequest req) {
        Patient p = new Patient();

        // ID (seulement pour modification)
        String idStr = req.getParameter("idPatient");
        if (idStr != null && !idStr.isEmpty() && ValidationUtils.isValidId(idStr)) {
            p.setIdPatient(Integer.parseInt(idStr));
        }

        // Nom
        String nom = req.getParameter("nomPatient");
        if (nom != null) p.setNomPatient(nom.trim());

        // Prénom
        String prenom = req.getParameter("prenomPatient");
        if (prenom != null) p.setPrenomPatient(prenom.trim());

        // Date de naissance
        String dateStr = req.getParameter("dateNaissance");
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                p.setDateNaissance(Date.valueOf(dateStr));
            } catch (IllegalArgumentException e) {
                // Date invalide ignorée
            }
        }

        // Téléphone
        String tel = req.getParameter("telephonePatient");
        if (tel != null && ValidationUtils.isValidPhone(tel)) {
            p.setTelephonePatient(tel.trim());
        }

        // Email
        String email = req.getParameter("emailPatient");
        if (email != null && ValidationUtils.isValidEmail(email)) {
            p.setEmailPatient(email.trim());
        }

        // Adresse
        String adresse = req.getParameter("adresse");
        if (adresse != null) p.setAdresse(adresse.trim());

        // Groupe sanguin (liste blanche stricte)
        String groupe = req.getParameter("groupeSanguin");
        if (groupe != null && GROUPES_SANGUINS.contains(groupe)) {
            p.setGroupeSanguin(groupe);
        }

        return p;
    }

    public static boolean isValidStatutRdv(String statut) {
        return statut != null && STATUTS_RDV.contains(statut);
    }

    public static boolean isValidGroupeSanguin(String groupe) {
        return groupe != null && GROUPES_SANGUINS.contains(groupe);
    }
}