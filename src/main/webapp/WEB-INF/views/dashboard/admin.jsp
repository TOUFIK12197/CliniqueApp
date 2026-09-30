<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <div class="page-header">
        <h1>🏥 Tableau de bord</h1>
        <span class="badge role-admin">Administration</span>
    </div>

    <!-- Stats -->
    <div class="stats-grid">
        <div class="stat-card">
            <span class="stat-icon">👥</span>
            <div class="stat-number">${stats.nbPatient}</div>
            <div class="stat-label">Patients enregistrés</div>
        </div>
        <div class="stat-card green">
            <span class="stat-icon">📅</span>
            <div class="stat-number">${stats.nbRendezVous}</div>
            <div class="stat-label">Rendez-vous total</div>
        </div>
        <div class="stat-card purple">
            <span class="stat-icon">🩺</span>
            <div class="stat-number">${stats.nbconsultation}</div>
            <div class="stat-label">Consultations réalisées</div>
        </div>
        <div class="stat-card red">
            <span class="stat-icon">❌</span>
            <div class="stat-number">${stats.nbAnnulations}</div>
            <div class="stat-label">Annulations</div>
        </div>
    </div>

    <!-- Gestion système -->
    <p class="section-title">⚙️ Gestion du système</p>
    <div class="actions-grid">
        <a href="${pageContext.request.contextPath}/admin?action=medecins" class="action-card">
            <span class="action-icon">👨‍⚕️</span>
            <span>Médecins</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin?action=secretaires" class="action-card">
            <span class="action-icon">👩‍💼</span>
            <span>Secrétaires</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin?action=specialites" class="action-card">
            <span class="action-icon">🏷️</span>
            <span>Spécialités</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin?action=statistiques" class="action-card">
            <span class="action-icon">📊</span>
            <span>Statistiques</span>
        </a>
        <a href="${pageContext.request.contextPath}/patients" class="action-card">
            <span class="action-icon">👥</span>
            <span>Patients</span>
        </a>
        <a href="${pageContext.request.contextPath}/rendezvous" class="action-card">
            <span class="action-icon">📅</span>
            <span>Rendez-vous</span>
        </a>
    </div>

</main>
</body>
</html>