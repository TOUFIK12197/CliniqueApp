<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="clinique.util.CSRFUtils" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Upload Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .upload-zone {
            border: 2px dashed var(--primary);
            border-radius: 12px;
            padding: 3rem 2rem;
            text-align: center;
            cursor: pointer;
            transition: var(--transition);
            background: var(--primary-bg);
            position: relative;
        }
        .upload-zone:hover {
            background: #e0e7ff;
            border-color: var(--primary-dark);
        }
        .upload-zone.dragover {
            background: #e0e7ff;
            border-color: var(--primary-dark);
            transform: scale(1.01);
        }
        .upload-zone input[type="file"] {
            position: absolute;
            inset: 0;
            opacity: 0;
            cursor: pointer;
            width: 100%;
            height: 100%;
        }
        .upload-icon   { font-size: 3rem; display: block; margin-bottom: 0.75rem; }
        .upload-title  { font-size: 1.1rem; font-weight: 700; color: var(--primary); }
        .upload-hint   { font-size: 0.82rem; color: var(--gray-400); margin-top: 0.5rem; }
        .file-preview  {
            display: none;
            margin-top: 1rem;
            padding: 0.75rem 1rem;
            background: var(--success-bg);
            border-radius: 8px;
            color: #065f46;
            font-size: 0.88rem;
            font-weight: 600;
            align-items: center;
            gap: 0.5rem;
        }
        .type-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
            gap: 0.75rem;
            margin-top: 0.5rem;
        }
        .type-option {
            border: 2px solid var(--border);
            border-radius: 10px;
            padding: 0.75rem;
            text-align: center;
            cursor: pointer;
            transition: var(--transition);
        }
        .type-option:hover { border-color: var(--primary); background: var(--primary-bg); }
        .type-option input[type="radio"] { display: none; }
        .type-option.selected { border-color: var(--primary); background: var(--primary-bg); }
        .type-option-icon { font-size: 1.5rem; display: block; margin-bottom: 0.25rem; }
        .type-option-label { font-size: 0.8rem; font-weight: 600; color: var(--gray-700); }
        .formats-info {
            background: var(--warning-bg);
            border: 1px solid #fcd34d;
            border-radius: 8px;
            padding: 0.75rem 1rem;
            font-size: 0.82rem;
            color: #92400e;
            margin-top: 0.5rem;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/includes/navbar.jsp"/>
<main class="container">

    <div class="page-header">
        <h1>⬆️ Uploader un Document</h1>
        <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}"
           class="btn btn-secondary">⬅ Retour</a>
    </div>

    <!-- Info patient -->
    <div class="info-box">
        👤 Patient : <strong>${patient.prenomPatient} ${patient.nomPatient}</strong>
        &nbsp;|&nbsp;
        🩸 Groupe : <strong>${patient.groupeSanguin}</strong>
    </div>

    <c:if test="${not empty erreur}">
        <div class="alert alert-danger">${erreur}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/documents"
          method="post"
          enctype="multipart/form-data"
          class="form-card"
          id="uploadForm">

        <input type="hidden" name="csrfToken" value="<%= CSRFUtils.getToken(session) %>">
        <input type="hidden" name="idPatient" value="${patient.idPatient}">

        <!-- Zone upload -->
        <div class="form-group">
            <label>📎 Fichier *</label>
            <div class="upload-zone" id="uploadZone">
                <input type="file" name="fichier" id="fichierInput"
                       accept=".pdf,.jpg,.jpeg,.png,.doc,.docx" required>
                <span class="upload-icon">☁️</span>
                <div class="upload-title">Glissez votre fichier ici</div>
                <div class="upload-hint">ou cliquez pour parcourir</div>
            </div>
            <div class="file-preview" id="filePreview">
                ✅ <span id="fileName"></span> — <span id="fileSize"></span>
            </div>
            <div class="formats-info">
                ⚠️ Formats acceptés : PDF, JPG, PNG, DOC, DOCX &nbsp;|&nbsp; Taille max : 10 Mo
            </div>
        </div>

        <!-- Type de document -->
        <div class="form-group">
            <label>🏷️ Type de document *</label>
            <div class="type-grid">
                <label class="type-option" onclick="selectType(this)">
                    <input type="radio" name="typeDocument" value="RADIO" required>
                    <span class="type-option-icon">🔬</span>
                    <span class="type-option-label">Radiologie</span>
                </label>
                <label class="type-option" onclick="selectType(this)">
                    <input type="radio" name="typeDocument" value="ANALYSE">
                    <span class="type-option-icon">🧪</span>
                    <span class="type-option-label">Analyse</span>
                </label>
                <label class="type-option" onclick="selectType(this)">
                    <input type="radio" name="typeDocument" value="COMPTE_RENDU">
                    <span class="type-option-icon">📝</span>
                    <span class="type-option-label">Compte rendu</span>
                </label>
                <label class="type-option" onclick="selectType(this)">
                    <input type="radio" name="typeDocument" value="CERTIFICAT">
                    <span class="type-option-icon">📜</span>
                    <span class="type-option-label">Certificat</span>
                </label>
                <label class="type-option" onclick="selectType(this)">
                    <input type="radio" name="typeDocument" value="AUTRE">
                    <span class="type-option-icon">📄</span>
                    <span class="type-option-label">Autre</span>
                </label>
            </div>
        </div>

        <!-- Description -->
        <div class="form-group">
            <label>📋 Description</label>
            <textarea name="description" class="form-control" rows="3"
                      placeholder="Description du document (optionnel)..."></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary btn-lg">
                ⬆️ Uploader le document
            </button>
            <a href="${pageContext.request.contextPath}/documents?action=liste&idPatient=${patient.idPatient}"
               class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</main>

<script>
    // Aperçu du fichier sélectionné
    document.getElementById('fichierInput').addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            document.getElementById('fileName').textContent = file.name;
            document.getElementById('fileSize').textContent = formatSize(file.size);
            const preview = document.getElementById('filePreview');
            preview.style.display = 'flex';
        }
    });

    // Drag & Drop
    const zone = document.getElementById('uploadZone');
    zone.addEventListener('dragover', e => {
        e.preventDefault();
        zone.classList.add('dragover');
    });
    zone.addEventListener('dragleave', () => zone.classList.remove('dragover'));
    zone.addEventListener('drop', e => {
        e.preventDefault();
        zone.classList.remove('dragover');
        const file = e.dataTransfer.files[0];
        if (file) {
            document.getElementById('fichierInput').files = e.dataTransfer.files;
            document.getElementById('fileName').textContent = file.name;
            document.getElementById('fileSize').textContent = formatSize(file.size);
            document.getElementById('filePreview').style.display = 'flex';
        }
    });

    // Sélection type document
    function selectType(el) {
        document.querySelectorAll('.type-option').forEach(o => o.classList.remove('selected'));
        el.classList.add('selected');
    }

    // Formater la taille
    function formatSize(bytes) {
        if (bytes < 1024) return bytes + ' o';
        if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' Ko';
        return (bytes / (1024 * 1024)).toFixed(1) + ' Mo';
    }
</script>
</body>
</html>