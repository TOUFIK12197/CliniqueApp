<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${rdv == null ? 'Nouveau RDV' : 'Modifier RDV'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>${rdv == null ? '📅 Nouveau Rendez-vous' : '✏️ Modifier Rendez-vous'}</h1>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">${erreur}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/rendezvous" method="post" class="form-card">

        <%-- TOKEN CSRF --%>
        <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">

        <c:if test="${rdv != null}">
            <input type="hidden" name="idRdv" value="${rdv.idRdv}">
        </c:if>

        <c:if test="${rdv == null}">
            <div class="form-group">
                <label>Patient *</label>
                <select name="idPatient" class="form-control" required>
                    <option value="">-- Sélectionner un patient --</option>
                    <c:forEach var="p" items="${patients}">
                        <option value="${p.idPatient}">
                            ${p.prenomPatient} ${p.nomPatient}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Médecin *</label>
                <select name="idMedecin" class="form-control" required>
                    <option value="">-- Sélectionner un médecin --</option>
                    <c:forEach var="m" items="${medecins}">
                        <option value="${m.idUtilisateur}">
                            Dr. ${m.prenomUtilisateur} ${m.nomUtilisateur}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </c:if>

        <div class="form-group">
            <label>Date et heure *</label>
            <input type="datetime-local" name="dateHeure" class="form-control" required>
        </div>

        <div class="form-group">
            <label>Motif</label>
            <input type="text" name="motif" class="form-control"
                   value="${rdv.motif}"
                   placeholder="Motif de la consultation">
        </div>

        <c:if test="${rdv != null}">
            <div class="form-group">
                <label>Statut</label>
                <select name="statut" class="form-control">
                    <option value="EN_ATTENTE"
                        ${rdv.statut == 'EN_ATTENTE' ? 'selected' : ''}>En attente</option>
                    <option value="CONFIRME"
                        ${rdv.statut == 'CONFIRME' ? 'selected' : ''}>Confirmé</option>
                    <option value="ANNULE"
                        ${rdv.statut == 'ANNULE' ? 'selected' : ''}>Annulé</option>
                </select>
            </div>
        </c:if>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                ${rdv == null ? '✅ Créer le RDV' : '💾 Enregistrer'}
            </button>
            <a href="${pageContext.request.contextPath}/rendezvous"
               class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>