<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Médecin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>
    <main class="container">
        <h1>Bonjour Dr. ${utilisateur.prenomUtilisateur} ${utilisateur.nomUtilisateur}</h1>
        <div class="actions-grid">
            <a href="${pageContext.request.contextPath}/rendezvous?action=planning" class="action-card">
                <span class="action-icon">📋</span>
                <span>Mon Planning</span>
            </a>
            <a href="${pageContext.request.contextPath}/patients" class="action-card">
                <span class="action-icon">👥</span>
                <span>Mes Patients</span>
            </a>
        </div>
    </main>
</body>
</html>