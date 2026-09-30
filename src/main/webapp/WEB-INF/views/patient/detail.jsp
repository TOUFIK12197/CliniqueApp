<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détail Patient</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <div class="page-header">
        <h1>🗂️ Dossier : ${patient.prenomPatient} ${patient.nomPatient}</h1>
    </div>

    <div class="detail-card">
        <div class="detail-row">
            <span class="detail-label">👤 Nom complet</span>
            <span>${patient.prenomPatient} ${patient.nomPatient}</span>
        </div>
        <div class="detail-row">
            <span class="detail-label">🎂 Date de naissance</span>
            <span>${patient.dateNaissance}</span>
        </div>
        <div class="detail-row">
            <span class="detail-label">📞 Téléphone</span>
            <span>${patient.telephonePatient}</span>
        </div>
        <div class="detail-row">
            <span class="detail-label">📧 Email</span>
            <span>${patient.emailPatient}</span>
        </div>
        <div class="detail-row">
            <span class="detail-label">🏠 Adresse</span>
            <span>${patient.adresse}</span>
        </div>
        <div class="detail-row">
            <span class="detail-label">🩸 Groupe sanguin</span>
            <span class="badge">${patient.groupeSanguin}</span>
        </div>
    </div>

    <div class="form-actions">
        <%-- Historique : tous les rôles --%>
        <a href="${pageContext.request.contextPath}/consultations?action=historique&idPatient=${patient.idPatient}"
           class="btn btn-info">📋 Historique des consultations</a>
           
           <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}"
             class="btn btn-info">📁 Documents médicaux</a>

        <%-- Modifier : Admin et Secrétaire seulement --%>
        <c:if test="${utilisateur.role != 'MEDECIN'}">
            <a href="${pageContext.request.contextPath}/patients?action=modifier&id=${patient.idPatient}"
               class="btn btn-warning">✏️ Modifier</a>
        </c:if>

        <a href="${pageContext.request.contextPath}/patients"
           class="btn btn-secondary">⬅ Retour</a>
    </div>
     
</main>
</body>
</html>