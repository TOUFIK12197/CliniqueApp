<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Médecins</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <div class="page-header">
        <h1>👨‍⚕️ Gestion des Médecins</h1>
        <a href="${pageContext.request.contextPath}/admin?action=nouveauMedecin"
           class="btn btn-primary">+ Nouveau médecin</a>
    </div>

    <!-- Messages -->
    <c:if test="${not empty sessionScope.erreur}">
        <div class="alert alert-danger">${sessionScope.erreur}</div>
        <c:remove var="erreur" scope="session"/>
    </c:if>

    <table class="table">
        <thead>
            <tr>
                <th>#</th>
                <th>Nom complet</th>
                <th>Login</th>
                <th>N° Ordre</th>
                <th>Téléphone</th>
                <th>Email</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="m" items="${medecins}">
            <tr>
                <td>${m.idUtilisateur}</td>
                <td>Dr. ${m.prenomUtilisateur} ${m.nomUtilisateur}</td>
                <td><code>${m.login}</code></td>
                <td>${m.numOrdre}</td>
                <td>${m.telephoneUtilisateur}</td>
                <td>${m.emailUtilisateur}</td>
                <td>
                    <c:choose>
                        <c:when test="${m.actif}">
                            <span class="badge badge-confirme">✅ Actif</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-annule">❌ Inactif</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/admin?action=modifierMedecin&id=${m.idUtilisateur}"
                       class="btn btn-sm btn-warning">✏️ Modifier</a>

                    <a href="${pageContext.request.contextPath}/admin?action=toggleMedecin&id=${m.idUtilisateur}"
                       class="btn btn-sm ${m.actif ? 'btn-secondary' : 'btn-success'}">
                        ${m.actif ? '🔒 Désactiver' : '🔓 Activer'}
                    </a>

                    <a href="${pageContext.request.contextPath}/admin?action=supprimerMedecin&id=${m.idUtilisateur}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Supprimer le Dr. ${m.nomUtilisateur} ?')">
                        🗑️ Supprimer
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty medecins}">
            <tr><td colspan="8" class="text-center">Aucun médecin enregistré.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">⬅ Retour</a>
    </div>
</main>
</body>
</html>