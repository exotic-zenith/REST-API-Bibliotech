# BiblioTech API

API REST Spring Boot 3 pour la gestion d'une bibliotheque, avec:
- Architecture en couches (web, service, data)
- JPA + transactions + audit
- DTO + MapStruct
- Securite JWT + roles ADMIN/USER
- Scheduler pour les emprunts OVERDUE
- Complement MongoDB reactif (profil `mongo`)

## Identifiants de demo
- `admin` / `admin123`
- `user` / `user123`

## Lancer les tests
```powershell
.\mvnw.cmd test
```

## Lancer l'application
```powershell
.\mvnw.cmd spring-boot:run
```

## Interface simple (web)
Flux en 2 pages:
- `http://localhost:8080/` -> redirection automatique
- `http://localhost:8080/login.html` -> page de connexion
- `http://localhost:8080/manage.html` -> gestion BiblioTech

Depuis l'interface de gestion vous pouvez:
- lister les livres
- ajouter un livre
- supprimer un livre
- vous deconnecter

## Obtenir un token JWT
```powershell
$body = '{"username":"admin","password":"admin123"}'
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/auth/token" -ContentType "application/json" -Body $body
```

## Activer le mode Mongo reactive
Configurer `spring.data.mongodb.uri` puis activer le profil `mongo`.
