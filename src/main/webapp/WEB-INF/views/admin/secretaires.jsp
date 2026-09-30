<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Secrétaires</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <div class="page-header">
        <h1>👩‍💼 Gestion des Secrétaires</h1>
        <a href="${pageContext.request.contextPath}/admin?action=nouvelleSecretaire"
           class="btn btn-primary">+ Nouvelle secrétaire</a>
    </div>

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
                <th>Téléphone</th>
                <th>Email</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${secretaires}">
            <tr>
                <td>${s.idUtilisateur}</td>
                <td>${s.prenomUtilisateur} ${s.nomUtilisateur}</td>
                <td><code>${s.login}</code></td>
                <td>${s.telephoneUtilisateur}</td>
                <td>${s.emailUtilisateur}</td>
                <td>
                    <c:choose>
                        <c:when test="${s.actif}">
                            <span class="badge badge-confirme">✅ Active</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-annule">❌ Inactive</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/admin?action=modifierSecretaire&id=${s.idUtilisateur}"
                       class="btn btn-sm btn-warning">✏️ Modifier</a>

                    <a href="${pageContext.request.contextPath}/admin?action=toggleSecretaire&id=${s.idUtilisateur}"
                       class="btn btn-sm ${s.actif ? 'btn-secondary' : 'btn-success'}">
                        ${s.actif ? '🔒 Désactiver' : '🔓 Activer'}
                    </a>

                    <a href="${pageContext.request.contextPath}/admin?action=supprimerSecretaire&id=${s.idUtilisateur}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Supprimer ${s.prenomUtilisateur} ${s.nomUtilisateur} ?')">
                        🗑️ Supprimer
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty secretaires}">
            <tr><td colspan="7" class="text-center">Aucune secrétaire enregistrée.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">⬅ Retour</a>
    </div>
</main>
</body>
</html>