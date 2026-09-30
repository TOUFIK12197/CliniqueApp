<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Clinique SantéPlus</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-page">
    <div class="login-container">
        <div class="login-header">
            <h1>🏥 Clinique SantéPlus</h1>
            <p>Système de Gestion Médicale</p>
        </div>
        <div class="login-form">
            <h2>Connexion</h2>
            <% if (request.getAttribute("erreur") != null) { %>
                <div class="alert alert-danger">${erreur}</div>
            <% } %>
            <form action="${pageContext.request.contextPath}/auth" method="post">
                <div class="form-group">
                    <label for="login">Identifiant</label>
                    <input type="text" id="login" name="login" 
                           class="form-control" required placeholder="Votre identifiant">
                </div>
                <div class="form-group">
                    <label for="motDePasse">Mot de passe</label>
                    <input type="password" id="motDePasse" name="motDePasse" 
                           class="form-control" required placeholder="Votre mot de passe">
                </div>
                <button type="submit" class="btn btn-primary btn-block">Se connecter</button>
            </form>
        </div>
    </div>
</body>
</html>