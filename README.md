# Clinique SantéPlus

Application web de gestion d'une clinique, développée en Java avec des pages JSP. Elle permet au personnel de gérer les patients, les rendez-vous et les consultations depuis une interface adaptée à son rôle.

## Fonctionnalités

- Authentification et tableaux de bord selon le profil : administrateur, médecin ou secrétaire.
- Gestion des médecins, des secrétaires et des spécialités par l'administrateur.
- Gestion, recherche et consultation des dossiers patients.
- Création et suivi des rendez-vous, avec affichage du planning.
- Saisie des consultations et accès à leur historique.
- Gestion de documents médicaux associés aux patients.
- Statistiques d'activité de la clinique.
- Contrôles de sécurité applicatifs, notamment protection CSRF, contrôle d'accès et validation des entrées.

## Technologies

- Java 21
- Jakarta Servlet 6 / JSP
- Apache Tomcat 10.1
- MySQL
- JSTL 3
- HTML et CSS

Le projet est structuré comme un projet web dynamique Eclipse (WTP). Il ne contient pas de fichier Maven ou Gradle.

## Prérequis

- JDK 21
- Apache Tomcat 10.1
- MySQL Server
- Eclipse IDE avec les outils Enterprise Java et Web (ou un environnement équivalent)

## Installation et lancement

1. Clonez le dépôt :

   ```bash
   git clone <URL_DU_DEPOT>
   cd CliniqueApp
   ```

2. Créez la base de données et ses tables en exécutant le script [Script base de donné.sql.txt](./Script%20base%20de%20donn%C3%A9.sql.txt) dans MySQL Workbench ou un client MySQL. Le script crée la base `clinique_santeplus` et insère des données d'exemple.

3. Configurez l'accès à MySQL dans [DBConnection.java](./src/main/java/clinique/dao/DBConnection.java) : vérifiez l'URL, le nom d'utilisateur et le mot de passe de votre serveur MySQL.

4. Importez le projet dans Eclipse comme projet existant, associez un JDK 21 et un runtime Apache Tomcat 10.1, puis publiez/lancez le projet sur le serveur.

5. Ouvrez l'application à l'adresse correspondant au contexte de déploiement, généralement :

   ```text
   http://localhost:8080/CliniqueApp/
   ```

   L'application redirige vers la page de connexion.

Les bibliothèques JSTL et le pilote MySQL Connector/J sont fournis dans `src/main/webapp/WEB-INF/lib`. Les API Servlet/JSP sont normalement fournies par le runtime Tomcat.

## Structure du projet

```text
src/main/java/clinique/
├── controller/   Servlets et traitements des requêtes
├── dao/          Accès à la base de données
├── filter/       Filtres d'authentification et de sécurité
├── model/        Modèles métier
└── util/         Validation et utilitaires

src/main/webapp/
├── WEB-INF/views/ Pages JSP
├── WEB-INF/web.xml
└── css/           Feuilles de style

Script base de donné.sql.txt   Création de la base et données de démonstration
```

## Données de démonstration et sécurité

Le script SQL ajoute des comptes et des données fictifs pour faciliter les essais. Consultez le script pour les identifiants de démonstration et supprimez ou remplacez ces comptes avant tout déploiement accessible à des tiers.

Ce projet est destiné à la démonstration et à l'apprentissage. Ne l'utilisez pas en production ni avec de véritables données médicales sans audit, configuration sécurisée des secrets, HTTPS et vérification des exigences légales applicables.

## Licence

Aucune licence n'est spécifiée pour le moment. Ajoutez un fichier de licence si vous souhaitez définir les conditions de réutilisation du projet.
