<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Secrétaire</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>
    <main class="container">
        <h1>Espace Secrétariat</h1>
        <div class="actions-grid">
            <a href="${pageContext.request.contextPath}/patients?action=nouveau" class="action-card">
                <span class="action-icon">➕</span>
                <span>Nouveau Patient</span>
            </a>
            <a href="${pageContext.request.contextPath}/patients" class="action-card">
                <span class="action-icon">👥</span>
                <span>Liste des Patients</span>
            </a>
            <a href="${pageContext.request.contextPath}/rendezvous?action=nouveau" class="action-card">
                <span class="action-icon">📅</span>
                <span>Nouveau RDV</span>
            </a>
            <a href="${pageContext.request.contextPath}/rendezvous" class="action-card">
                <span class="action-icon">📋</span>
                <span>Tous les RDV</span>
            </a>
        </div>
    </main>
</body>
</html>