<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
    <div class="nav-brand">🏥 Clinique SantéPlus</div>

    <ul class="nav-menu">
        <li><a href="${pageContext.request.contextPath}/dashboard">🏠 Accueil</a></li>

        <%-- ADMIN --%>
        <c:if test="${utilisateur.role == 'ADMIN'}">
            <li class="has-dropdown">
                <a href="${pageContext.request.contextPath}/admin?action=medecins">👨‍⚕️ Médecins</a>
                <ul class="dropdown">
                    <li><a href="${pageContext.request.contextPath}/admin?action=medecins">📋 Liste des médecins</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin?action=nouveauMedecin">➕ Nouveau médecin</a></li>
                </ul>
            </li>
            <li class="has-dropdown">
                <a href="${pageContext.request.contextPath}/admin?action=secretaires">👩‍💼 Secrétaires</a>
                <ul class="dropdown">
                    <li><a href="${pageContext.request.contextPath}/admin?action=secretaires">📋 Liste des secrétaires</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin?action=nouvelleSecretaire">➕ Nouvelle secrétaire</a></li>
                </ul>
            </li>
            <li><a href="${pageContext.request.contextPath}/admin?action=specialites">🏷️ Spécialités</a></li>
            <li><a href="${pageContext.request.contextPath}/admin?action=statistiques">📊 Statistiques</a></li>
            <li><a href="${pageContext.request.contextPath}/patients">👥 Patients</a></li>
            <li><a href="${pageContext.request.contextPath}/rendezvous">📅 RDV</a></li>
        </c:if>

        <%-- SECRÉTAIRE --%>
        <c:if test="${utilisateur.role == 'SECRETAIRE'}">
            <li><a href="${pageContext.request.contextPath}/patients">👥 Patients</a></li>
            <li><a href="${pageContext.request.contextPath}/rendezvous">📅 Rendez-vous</a></li>
        </c:if>

        <%-- MÉDECIN --%>
        <c:if test="${utilisateur.role == 'MEDECIN'}">
            <li><a href="${pageContext.request.contextPath}/rendezvous?action=planning">📋 Mon Planning</a></li>
            <li><a href="${pageContext.request.contextPath}/patients">👥 Patients</a></li>
        </c:if>
    </ul>

    <div class="nav-user">
        <span class="role-badge role-${utilisateur.role.toLowerCase()}">${utilisateur.role}</span>
        <span>👤 ${utilisateur.prenomUtilisateur} ${utilisateur.nomUtilisateur}</span>
        <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">🚪 Déconnexion</a>
    </div>
</nav>