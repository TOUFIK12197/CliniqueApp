<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Mon Planning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>
    <main class="container">
        <h1>📋 Planning du ${date}</h1>
        <form action="${pageContext.request.contextPath}/rendezvous" method="get" class="search-form">
            <input type="hidden" name="action" value="planning">
            <input type="date" name="date" value="${date}" class="form-control">
            <button type="submit" class="btn btn-primary">Afficher</button>
        </form>

        <c:if test="${empty planning}">
            <div class="alert alert-info">Aucun rendez-vous pour cette date.</div>
        </c:if>
        <c:forEach var="rdv" items="${planning}">
            <div class="planning-card">
                <div class="planning-time">${rdv.dateHeure}</div>
                <div class="planning-info">
                    <strong>${rdv.patient.prenomPatient} ${rdv.patient.nomPatient}</strong>
                    <span>${rdv.motif}</span>
                    <span class="badge badge-${rdv.statut.toLowerCase()}">${rdv.statut}</span>
                </div>
                <div class="planning-actions">
                    <c:if test="${rdv.statut == 'CONFIRME'}">
                        <a href="${pageContext.request.contextPath}/consultations?action=nouveau&idRdv=${rdv.idRdv}"
                           class="btn btn-sm btn-success">Enregistrer consultation</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/consultations?action=historique&idPatient=${rdv.patient.idPatient}"
                       class="btn btn-sm btn-info">Historique patient</a>
                </div>
            </div>
        </c:forEach>
    </main>
</body>
</html>