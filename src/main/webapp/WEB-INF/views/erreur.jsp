<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Erreur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="error-page">
        <h1>⚠️ Une erreur est survenue</h1>
        <p>Veuillez contacter l'administrateur.</p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Retour à l'accueil</a>
    </div>
</body>
</html>