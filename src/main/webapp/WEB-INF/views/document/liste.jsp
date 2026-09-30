<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Documents Médicaux</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .doc-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 1rem;
            margin-top: 1rem;
        }
        .doc-card {
            background: white;
            border-radius: 12px;
            padding: 1.25rem;
            border: 1px solid var(--border);
            box-shadow: var(--shadow-sm);
            transition: var(--transition);
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
        }
        .doc-card:hover {
            box-shadow: var(--shadow);
            transform: translateY(-2px);
        }
        .doc-card-header {
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }
        .doc-icon {
            font-size: 2.5rem;
            flex-shrink: 0;
        }
        .doc-name {
            font-weight: 600;
            color: var(--dark);
            font-size: 0.9rem;
            word-break: break-word;
        }
        .doc-meta {
            font-size: 0.78rem;
            color: var(--gray-400);
            display: flex;
            flex-direction: column;
            gap: 0.2rem;
        }
        .doc-description {
            font-size: 0.85rem;
            color: var(--gray-600);
            background: var(--gray-100);
            padding: 0.5rem 0.75rem;
            border-radius: 6px;
            min-height: 40px;
        }
        .doc-actions {
            display: flex;
            gap: 0.5rem;
            margin-top: auto;
            padding-top: 0.5rem;
            border-top: 1px solid var(--border);
        }
        .filter-bar {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
            margin-bottom: 1.5rem;
        }
        .filter-btn {
            padding: 0.4rem 1rem;
            border-radius: 20px;
            border: 1.5px solid var(--border);
            background: white;
            color: var(--gray-600);
            cursor: pointer;
            font-size: 0.82rem;
            font-weight: 600;
            text-decoration: none;
            transition: var(--transition);
        }
        .filter-btn:hover,
        .filter-btn.active {
            background: var(--primary);
            color: white;
            border-color: var(--primary);
        }
        .type-badge {
            display: inline-block;
            padding: 0.2rem 0.6rem;
            border-radius: 20px;
            font-size: 0.72rem;
            font-weight: 700;
            text-transform: uppercase;
        }
        .type-RADIO         { background: #dbeafe; color: #1e40af; }
        .type-ANALYSE       { background: #d1fae5; color: #065f46; }
        .type-COMPTE_RENDU  { background: #ede9fe; color: #5b21b6; }
        .type-CERTIFICAT    { background: #fef3c7; color: #92400e; }
        .type-AUTRE         { background: var(--gray-100); color: var(--gray-600); }
        .stats-bar {
            display: flex;
            gap: 1rem;
            margin-bottom: 1.5rem;
            flex-wrap: wrap;
        }
        .stats-pill {
            background: white;
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 0.4rem 1rem;
            font-size: 0.82rem;
            font-weight: 600;
            color: var(--gray-600);
        }
        .preview-img {
            width: 100%;
            height: 120px;
            object-fit: cover;
            border-radius: 6px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <!-- En-tête -->
    <div class="page-header">
        <div>
            <h1>📁 Documents Médicaux</h1>
            <p style="color: var(--gray-500); margin-top: 0.25rem; font-size: 0.9rem;">
                Patient : <strong>${patient.prenomPatient} ${patient.nomPatient}</strong>
            </p>
        </div>
        <div style="display:flex; gap:0.75rem;">
            <a href="${pageContext.request.contextPath}/documents?action=nouveau&idPatient=${patient.idPatient}"
               class="btn btn-primary">⬆️ Uploader un document</a>
            <a href="${pageContext.request.contextPath}/patients?action=detail&id=${patient.idPatient}"
               class="btn btn-secondary">⬅ Retour</a>
        </div>
    </div>

    <!-- Messages -->
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.erreur}">
        <div class="alert alert-danger">${sessionScope.erreur}</div>
        <c:remove var="erreur" scope="session"/>
    </c:if>

    <!-- Stats -->
    <div class="stats-bar">
        <span class="stats-pill">📄 Total : ${nbDocuments} document(s)</span>
    </div>

    <!-- Filtres par type -->
    <div class="filter-bar">
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}"
           class="filter-btn ${empty filtreType ? 'active' : ''}">Tous</a>
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}&type=RADIO"
           class="filter-btn ${filtreType == 'RADIO' ? 'active' : ''}">🔬 Radiologies</a>
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}&type=ANALYSE"
           class="filter-btn ${filtreType == 'ANALYSE' ? 'active' : ''}">🧪 Analyses</a>
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}&type=COMPTE_RENDU"
           class="filter-btn ${filtreType == 'COMPTE_RENDU' ? 'active' : ''}">📝 Comptes rendus</a>
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}&type=CERTIFICAT"
           class="filter-btn ${filtreType == 'CERTIFICAT' ? 'active' : ''}">📜 Certificats</a>
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}&type=AUTRE"
           class="filter-btn ${filtreType == 'AUTRE' ? 'active' : ''}">📄 Autres</a>
    </div>

    <!-- Grille des documents -->
    <c:if test="${empty documents}">
        <div class="empty-state">
            <span class="empty-state-icon">📂</span>
            <p class="empty-state-text">Aucun document pour ce patient.</p>
            <a href="${pageContext.request.contextPath}/documents?action=nouveau&idPatient=${patient.idPatient}"
               class="btn btn-primary" style="margin-top:1rem;">⬆️ Uploader le premier document</a>
        </div>
    </c:if>

    <div class="doc-grid">
        <c:forEach var="doc" items="${documents}">
            <div class="doc-card">
                <div class="doc-card-header">
                    <span class="doc-icon">${doc.icone}</span>
                    <div style="flex:1; min-width:0;">
                        <div class="doc-name">${doc.nomOriginal}</div>
                        <div class="doc-meta">
                            <span>📅 ${doc.dateUpload}</span>
                            <span>💾 ${doc.tailleFormatee}</span>
                            <span>👤 ${doc.uploadeur.prenomUtilisateur} ${doc.uploadeur.nomUtilisateur}</span>
                        </div>
                    </div>
                </div>

                <!-- Badge type -->
                <span class="type-badge type-${doc.typeDocument}">${doc.typeDocument}</span>

                <!-- Aperçu image -->
                <c:if test="${doc.image}">
                    <img src="${pageContext.request.contextPath}/documents?action=apercu&id=${doc.idDocument}"
                         class="preview-img" alt="${doc.nomOriginal}"
                         onclick="window.open(this.src, '_blank')">
                </c:if>

                <!-- Description -->
                <c:if test="${not empty doc.description}">
                    <div class="doc-description">${doc.description}</div>
                </c:if>

                <!-- Actions -->
                <div class="doc-actions">
                    <c:if test="${doc.pdf || doc.image}">
                        <a href="${pageContext.request.contextPath}/documents?action=apercu&id=${doc.idDocument}"
                           target="_blank" class="btn btn-sm btn-info">👁 Aperçu</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/documents?action=telecharger&id=${doc.idDocument}"
                       class="btn btn-sm btn-primary">⬇️ Télécharger</a>
                    <c:if test="${utilisateur.role == 'ADMIN' || utilisateur.role == 'MEDECIN'}">
                        <a href="${pageContext.request.contextPath}/documents?action=supprimer&id=${doc.idDocument}"
                           class="btn btn-sm btn-danger"
                           onclick="return confirm('Supprimer ce document ?')">🗑️</a>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

</main>
</body>
</html>