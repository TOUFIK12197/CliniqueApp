<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Spécialités</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>🏷️ Gestion des Spécialités</h1>

    <c:if test="${not empty sessionScope.erreur}">
        <div class="alert alert-danger">${sessionScope.erreur}</div>
        <c:remove var="erreur" scope="session"/>
    </c:if>

    <%-- Formulaire ajout / modification --%>
    <div class="form-card" style="margin-bottom: 2rem;">
        <h2>
            ${specialite != null ? '✏️ Modifier la spécialité' : '➕ Ajouter une spécialité'}
        </h2>
        <form action="${pageContext.request.contextPath}/admin" method="post">

            <%-- TOKEN CSRF --%>
            <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">
            <input type="hidden" name="action"    value="sauvegarderSpecialite">

            <c:if test="${specialite != null}">
                <input type="hidden" name="idspecialite" value="${specialite.idspecialite}">
            </c:if>

            <div class="form-row">
                <div class="form-group">
                    <label>Libellé *</label>
                    <input type="text" name="libelle" class="form-control" required
                           value="${specialite.libelle}"
                           placeholder="Ex: Cardiologie">
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <input type="text" name="description" class="form-control"
                           value="${specialite.description}"
                           placeholder="Description courte">
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    ${specialite != null ? '💾 Modifier' : '✅ Ajouter'}
                </button>
                <c:if test="${specialite != null}">
                    <a href="${pageContext.request.contextPath}/admin?action=specialites"
                       class="btn btn-secondary">Annuler</a>
                </c:if>
            </div>
        </form>
    </div>

    <%-- Liste des spécialités --%>
    <table class="table">
        <thead>
            <tr>
                <th>#</th>
                <th>Libellé</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${specialites}">
            <tr>
                <td>${s.idspecialite}</td>
                <td><strong>${s.libelle}</strong></td>
                <td>${s.description}</td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/admin?action=modifierSpecialite&id=${s.idspecialite}"
                       class="btn btn-sm btn-warning">✏️ Modifier</a>
                    <a href="${pageContext.request.contextPath}/admin?action=supprimerSpecialite&id=${s.idspecialite}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Supprimer ${s.libelle} ?')">
                        🗑️ Supprimer
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty specialites}">
            <tr>
                <td colspan="4" class="text-center">Aucune spécialité enregistrée.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/dashboard"
           class="btn btn-secondary">⬅ Retour</a>
    </div>
</main>
</body>
</html>