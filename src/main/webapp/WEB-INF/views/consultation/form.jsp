<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouvelle Consultation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>🩺 Enregistrer une Consultation</h1>

    <c:if test="${rdv != null}">
        <div class="info-box">
            👤 Patient : <strong>${rdv.patient.prenomPatient} ${rdv.patient.nomPatient}</strong>
            &nbsp;|&nbsp;
            📅 RDV du : <strong>${rdv.dateHeure}</strong>
            &nbsp;|&nbsp;
            📝 Motif : ${rdv.motif}
        </div>
    </c:if>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">${erreur}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/consultations" method="post" class="form-card">

        <%-- TOKEN CSRF --%>
        <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">

        <input type="hidden" name="idRdv"     value="${rdv.idRdv}">
        <input type="hidden" name="idPatient" value="${rdv.patient.idPatient}">

        <div class="form-row">
            <div class="form-group">
                <label>⚖️ Poids (kg)</label>
                <input type="number" step="0.1" min="0" max="300"
                       name="poids" class="form-control"
                       placeholder="Ex: 70.5">
            </div>
            <div class="form-group">
                <label>💉 Tension artérielle</label>
                <input type="text" name="tension" class="form-control"
                       placeholder="Ex: 12/8">
            </div>
        </div>

        <div class="form-group">
            <label>🔬 Diagnostic *</label>
            <textarea name="diagnostic" class="form-control" rows="3"
                      required placeholder="Diagnostic médical..."></textarea>
        </div>

        <div class="form-group">
            <label>💊 Prescription</label>
            <textarea name="prescription" class="form-control" rows="3"
                      placeholder="Médicaments prescrits..."></textarea>
        </div>

        <div class="form-group">
            <label>📝 Notes libres</label>
            <textarea name="notes" class="form-control" rows="2"
                      placeholder="Observations complémentaires..."></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                ✅ Enregistrer la consultation
            </button>
            <a href="${pageContext.request.contextPath}/rendezvous?action=planning"
               class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>