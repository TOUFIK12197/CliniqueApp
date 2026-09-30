<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${secretaire == null ? 'Nouvelle Secrétaire' : 'Modifier Secrétaire'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>
        ${secretaire == null ? '➕ Nouvelle Secrétaire' : '✏️ Modifier Secrétaire'}
    </h1>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">${erreur}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin" method="post" class="form-card">

        <%-- TOKEN CSRF --%>
        <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">
        <input type="hidden" name="action"    value="sauvegarderSecretaire">

        <c:if test="${secretaire != null}">
            <input type="hidden" name="idUtilisateur" value="${secretaire.idUtilisateur}">
        </c:if>

        <h3>👤 Informations personnelles</h3>
        <div class="form-row">
            <div class="form-group">
                <label>Nom *</label>
                <input type="text" name="nom" class="form-control"
                       value="${secretaire.nomUtilisateur}" required
                       placeholder="Nom de famille">
            </div>
            <div class="form-group">
                <label>Prénom *</label>
                <input type="text" name="prenom" class="form-control"
                       value="${secretaire.prenomUtilisateur}" required
                       placeholder="Prénom">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Téléphone</label>
                <input type="tel" name="telephone" class="form-control"
                       value="${secretaire.telephoneUtilisateur}"
                       placeholder="0600000000">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" class="form-control"
                       value="${secretaire.emailUtilisateur}"
                       placeholder="secretaire@clinique.fr">
            </div>
        </div>

        <hr>
        <h3>🔐 Accès au système</h3>
        <div class="form-row">
            <div class="form-group">
                <label>Login *</label>
                <input type="text" name="login" class="form-control"
                       value="${secretaire.login}" required
                       placeholder="secretaire1">
            </div>
            <c:if test="${secretaire == null}">
                <div class="form-group">
                    <label>Mot de passe *</label>
                    <input type="password" name="motDePasse" class="form-control"
                           required placeholder="Min 8 car. + 1 majuscule + 1 chiffre">
                </div>
            </c:if>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                ${secretaire == null ? '✅ Créer le compte' : '💾 Enregistrer'}
            </button>
            <a href="${pageContext.request.contextPath}/admin?action=secretaires"
               class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>