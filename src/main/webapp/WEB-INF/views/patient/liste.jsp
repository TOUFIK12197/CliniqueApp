<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Patients</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <div class="page-header">
        <h1>
            <c:choose>
                <c:when test="${utilisateur.role == 'MEDECIN'}">
                    👥 Mes Patients
                </c:when>
                <c:otherwise>
                    👥 Tous les Patients
                </c:otherwise>
            </c:choose>
        </h1>
        <%-- Bouton Nouveau patient : uniquement Admin et Secrétaire --%>
        <c:if test="${utilisateur.role != 'MEDECIN'}">
            <a href="${pageContext.request.contextPath}/patients?action=nouveau"
               class="btn btn-primary">+ Nouveau patient</a>
        </c:if>
    </div>

    <%-- Message d'info pour le médecin --%>
    <c:if test="${utilisateur.role == 'MEDECIN'}">
        <div class="info-box">
            📋 Vous consultez uniquement la liste de <strong>vos patients</strong>
            (patients ayant eu un rendez-vous avec vous).
        </div>
    </c:if>

    <%-- Messages session --%>
    <c:if test="${not empty sessionScope.erreur}">
        <div class="alert alert-danger">${sessionScope.erreur}</div>
        <c:remove var="erreur" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <%-- Barre de recherche --%>
    <form action="${pageContext.request.contextPath}/patients"
          method="get" class="search-form">
        <input type="hidden" name="action" value="rechercher">
        <input type="text" name="terme" value="${terme}"
               class="form-control"
               placeholder="Rechercher par nom, prénom ou email...">
        <button type="submit" class="btn btn-secondary">🔍 Rechercher</button>
        <a href="${pageContext.request.contextPath}/patients"
           class="btn btn-secondary">↺ Réinitialiser</a>
     <%@ page import="clinique.util.CSRFUtils" %>

    </form>

    <%-- Tableau des patients --%>
    <table class="table">
        <thead>
            <tr>
                <th>#</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Date de naissance</th>
                <th>Téléphone</th>
                <th>Email</th>
                <th>Groupe sanguin</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${patients}">
            <tr>
                <td>${p.idPatient}</td>
                <td><strong>${p.nomPatient}</strong></td>
                <td>${p.prenomPatient}</td>
                <td>${p.dateNaissance}</td>
                <td>${p.telephonePatient}</td>
                <td>${p.emailPatient}</td>
                <td><span class="badge">${p.groupeSanguin}</span></td>
                <td class="actions">
                    <%-- Détail : tous les rôles --%>
                    <a href="${pageContext.request.contextPath}/patients?action=detail&id=${p.idPatient}"
                       class="btn btn-sm btn-info">👁 Détail</a>

                    <%-- Historique consultations : tous les rôles --%>
                    <a href="${pageContext.request.contextPath}/consultations?action=historique&idPatient=${p.idPatient}"
                       class="btn btn-sm btn-secondary">📋 Historique</a>

                    <%-- Modifier et Supprimer : Admin et Secrétaire seulement --%>
                    <c:if test="${utilisateur.role != 'MEDECIN'}">
                        <a href="${pageContext.request.contextPath}/patients?action=modifier&id=${p.idPatient}"
                           class="btn btn-sm btn-warning">✏️ Modifier</a>
                        <a href="${pageContext.request.contextPath}/patients?action=supprimer&id=${p.idPatient}"
                           class="btn btn-sm btn-danger"
                           onclick="return confirm('Supprimer ${p.prenomPatient} ${p.nomPatient} ?')">
                            🗑️ Supprimer
                        </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty patients}">
            <tr>
                <td colspan="8" class="text-center" style="padding: 2rem; color: var(--secondary);">
                    <c:choose>
                        <c:when test="${utilisateur.role == 'MEDECIN'}">
                            Aucun patient associé à votre compte pour le moment.
                        </c:when>
                        <c:otherwise>
                            Aucun patient trouvé.
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <%-- Compteur --%>
    <p style="color: var(--secondary); font-size: 0.88rem;">
        Total : <strong>${patients.size()} patient(s)</strong>
        <c:if test="${utilisateur.role == 'MEDECIN'}"> associé(s) à votre compte</c:if>
    </p>

</main>
</body>
</html>