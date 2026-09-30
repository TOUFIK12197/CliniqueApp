<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${medecin == null ? 'Nouveau Médecin' : 'Modifier Médecin'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>
        ${medecin == null ? '➕ Nouveau Médecin' : '✏️ Modifier Médecin'}
    </h1>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">${erreur}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin" method="post" class="form-card">

        <%-- TOKEN CSRF --%>
        <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">
        <input type="hidden" name="action"    value="sauvegarderMedecin">

        <c:if test="${medecin != null}">
            <input type="hidden" name="idUtilisateur" value="${medecin.idUtilisateur}">
        </c:if>

        <h3>👤 Informations personnelles</h3>
        <div class="form-row">
            <div class="form-group">
                <label>Nom *</label>
                <input type="text" name="nom" class="form-control"
                       value="${medecin.nomUtilisateur}" required
                       placeholder="Nom de famille">
            </div>
            <div class="form-group">
                <label>Prénom *</label>
                <input type="text" name="prenom" class="form-control"
                       value="${medecin.prenomUtilisateur}" required
                       placeholder="Prénom">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Téléphone</label>
                <input type="tel" name="telephone" class="form-control"
                       value="${medecin.telephoneUtilisateur}"
                       placeholder="0600000000">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" class="form-control"
                       value="${medecin.emailUtilisateur}"
                       placeholder="medecin@clinique.fr">
            </div>
        </div>

        <hr>
        <h3>🔐 Accès au système</h3>
        <div class="form-row">
            <div class="form-group">
                <label>Login *</label>
                <input type="text" name="login" class="form-control"
                       value="${medecin.login}" required
                       placeholder="dr.dupont">
            </div>
            <c:if test="${medecin == null}">
                <div class="form-group">
                    <label>Mot de passe *</label>
                    <input type="password" name="motDePasse" class="form-control"
                           required placeholder="Min 8 car. + 1 majuscule + 1 chiffre">
                </div>
            </c:if>
        </div>

        <hr>
        <h3>🏥 Informations médicales</h3>
        <div class="form-row">
            <div class="form-group">
                <label>Numéro d'ordre *</label>
                <input type="text" name="numOrdre" class="form-control"
                       value="${medecin.numOrdre}" required
                       placeholder="ORD-2024-XXX">
            </div>
            <div class="form-group">
                <label>Spécialité</label>
                <select name="idSpecialite" class="form-control">
                    <option value="0">-- Aucune spécialité --</option>
                    <c:forEach var="s" items="${specialites}">
                        <option value="${s.idspecialite}"
                            ${idSpecActuelle == s.idspecialite ? 'selected' : ''}>
                            ${s.libelle}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                ${medecin == null ? '✅ Créer le médecin' : '💾 Enregistrer les modifications'}
            </button>
            <a href="${pageContext.request.contextPath}/admin?action=medecins"
               class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>