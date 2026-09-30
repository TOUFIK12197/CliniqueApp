<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Statistiques</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <h1>📊 Statistiques de la Clinique</h1>

    <!-- Chiffres globaux -->
    <h2>Chiffres globaux</h2>
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-number">${stats.nbPatient}</div>
            <div class="stat-label">👥 Patients</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${stats.nbRendezVous}</div>
            <div class="stat-label">📅 Rendez-vous</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${stats.nbconsultation}</div>
            <div class="stat-label">🩺 Consultations</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${stats.nbAnnulations}</div>
            <div class="stat-label">❌ Annulations</div>
        </div>
    </div>

    <!-- Rendez-vous par statut -->
    <h2>📅 Rendez-vous par statut</h2>
    <table class="table">
        <thead>
            <tr><th>Statut</th><th>Nombre</th></tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${rdvParStatut}">
            <tr>
                <td><span class="badge badge-${entry.key.toLowerCase()}">${entry.key}</span></td>
                <td><strong>${entry.value}</strong></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Consultations par médecin -->
    <h2>🩺 Consultations par médecin</h2>
    <table class="table">
        <thead>
            <tr>
                <th>Médecin</th>
                <th>Consultations</th>
                <th>Taux</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${statsParMedecin}">
            <tr>
                <td>Dr. ${entry.key}</td>
                <td><strong>${entry.value}</strong></td>
                <td>
                    <c:choose>
                        <c:when test="${stats.nbconsultation > 0}">
                            <div class="progress-bar">
                                <div class="progress-fill"
                                     style="width:${entry.value * 100 / stats.nbconsultation}%">
                                    ${entry.value * 100 / stats.nbconsultation}%
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width:0%">0%</div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty statsParMedecin}">
            <tr><td colspan="3" class="text-center">Aucune donnée.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">⬅ Retour</a>
    </div>
</main>
</body>
</html>