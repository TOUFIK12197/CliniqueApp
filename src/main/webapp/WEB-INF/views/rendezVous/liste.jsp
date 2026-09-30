<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Rendez-vous</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>
    <main class="container">
        <div class="page-header">
            <h1>📅 Rendez-vous</h1>
            <a href="${pageContext.request.contextPath}/rendezvous?action=nouveau" class="btn btn-primary">+ Nouveau RDV</a>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>Date & Heure</th>
                    <th>Patient</th>
                    <th>Médecin</th>
                    <th>Motif</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="rdv" items="${rdvList}">
                    <tr>
                        <td>${rdv.dateHeure}</td>
                        <td>${rdv.patient.prenomPatient} ${rdv.patient.nomPatient}</td>
                        <td>Dr. ${rdv.medecin.prenomUtilisateur} ${rdv.medecin.nomUtilisateur}</td>
                        <td>${rdv.motif}</td>
                        <td>
                            <span class="badge badge-${rdv.statut.toLowerCase()}">${rdv.statut}</span>
                        </td>
                        <td class="actions">
                            <c:if test="${rdv.statut != 'ANNULE' && rdv.statut != 'TERMINE'}">
                                <a href="${pageContext.request.contextPath}/rendezvous?action=modifier&id=${rdv.idRdv}" 
                                   class="btn btn-sm btn-warning">Modifier</a>
                                <a href="${pageContext.request.contextPath}/rendezvous?action=annuler&id=${rdv.idRdv}"
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('Annuler ce rendez-vous ?')">Annuler</a>
                            </c:if>
                            <c:if test="${rdv.statut == 'CONFIRME' && utilisateur.role == 'MEDECIN'}">
                                <a href="${pageContext.request.contextPath}/consultations?action=nouveau&idRdv=${rdv.idRdv}"
                                   class="btn btn-sm btn-success">Consultation</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty rdvList}">
                    <tr><td colspan="6" class="text-center">Aucun rendez-vous.</td></tr>
                </c:if>
            </tbody>
        </table>
    </main>
</body>
</html>