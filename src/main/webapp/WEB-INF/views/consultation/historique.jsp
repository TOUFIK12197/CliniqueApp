<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Historique Consultations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>
    <main class="container">
        <h1>📁 Dossier médical — ${patient.prenomPatient} ${patient.nomPatient}</h1>
        <div class="info-box">
            Groupe sanguin : <strong>${patient.groupeSanguin}</strong> | 
            Né(e) le : ${patient.dateNaissance}
        </div>

        <c:if test="${empty historique}">
            <div class="alert alert-info">Aucune consultation enregistrée pour ce patient.</div>
        </c:if>
        <c:forEach var="c" items="${historique}">
            <div class="consultation-card">
                <div class="consult-header">
                    <span class="consult-date">📅 ${c.dateConsult}</span>
                    <span>Dr. ${c.medecin.prenomUtilisateur} ${c.medecin.nomUtilisateur}</span>
                </div>
                <div class="consult-body">
                    <div class="consult-row"><strong>Diagnostic :</strong> ${c.diagnostic}</div>
                    <c:if test="${not empty c.tension}">
                        <div class="consult-row"><strong>Tension :</strong> ${c.tension}</div>
                    </c:if>
                    <c:if test="${c.poids != null}">
                        <div class="consult-row"><strong>Poids :</strong> ${c.poids} kg</div>
                    </c:if>
                    <c:if test="${not empty c.prescription}">
                        <div class="consult-row"><strong>Prescription :</strong> ${c.prescription}</div>
                    </c:if>
                    <c:if test="${not empty c.notes}">
                        <div class="consult-row"><strong>Notes :</strong> ${c.notes}</div>
                    </c:if>
                </div>
            </div>
        </c:forEach>
        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/patients" class="btn btn-secondary">Retour aux patients</a>
        </div>
    </main>
</body>
</html>