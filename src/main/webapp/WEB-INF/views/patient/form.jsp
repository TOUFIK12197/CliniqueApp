<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${patient == null ? 'Nouveau Patient' : 'Modifier Patient'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>${patient == null ? '➕ Nouveau Patient' : '✏️ Modifier Patient'}</h1>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">${erreur}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/patients" method="post" class="form-card">

        <%-- TOKEN CSRF --%>
        <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">

        <c:if test="${patient != null}">
            <input type="hidden" name="idPatient" value="${patient.idPatient}">
        </c:if>

        <div class="form-row">
            <div class="form-group">
                <label>Nom *</label>
                <input type="text" name="nomPatient" class="form-control"
                       value="${patient.nomPatient}" required
                       placeholder="Nom de famille">
            </div>
            <div class="form-group">
                <label>Prénom *</label>
                <input type="text" name="prenomPatient" class="form-control"
                       value="${patient.prenomPatient}" required
                       placeholder="Prénom">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Date de naissance</label>
                <input type="date" name="dateNaissance" class="form-control"
                       value="${patient.dateNaissance}">
            </div>
            <div class="form-group">
                <label>Groupe sanguin</label>
                <select name="groupeSanguin" class="form-control">
                    <option value="">-- Choisir --</option>
                    <%-- APRÈS (correct) --%>
                    <option value="A+"  ${patient.groupeSanguin == 'A+'  ? 'selected' : ''}>A+</option>
                    <option value="A-"  ${patient.groupeSanguin == 'A-'  ? 'selected' : ''}>A-</option>
                    <option value="B+"  ${patient.groupeSanguin == 'B+'  ? 'selected' : ''}>B+</option>
                    <option value="B-"  ${patient.groupeSanguin == 'B-'  ? 'selected' : ''}>B-</option>
                    <option value="AB+" ${patient.groupeSanguin == 'AB+' ? 'selected' : ''}>AB+</option>
                    <option value="AB-" ${patient.groupeSanguin == 'AB-' ? 'selected' : ''}>AB-</option>
                    <option value="O+"  ${patient.groupeSanguin == 'O+'  ? 'selected' : ''}>O+</option>
                    <option value="O-"  ${patient.groupeSanguin == 'O-'  ? 'selected' : ''}>O-</option>
                        <option value="${g}"
                            ${patient.groupeSanguin == g ? 'selected' : ''}>${g}</option>
                    
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Téléphone</label>
                <input type="tel" name="telephonePatient" class="form-control"
                       value="${patient.telephonePatient}"
                       placeholder="0600000000">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="emailPatient" class="form-control"
                       value="${patient.emailPatient}"
                       placeholder="patient@email.com">
            </div>
        </div>

        <div class="form-group">
            <label>Adresse</label>
            <textarea name="adresse" class="form-control"
                      rows="2">${patient.adresse}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                ${patient == null ? '✅ Enregistrer' : '💾 Mettre à jour'}
            </button>
            <a href="${pageContext.request.contextPath}/patients"
               class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>