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
Une interface minimale est disponible ici:
- `http://localhost:8080/`

Depuis cette page vous pouvez:
- lister les livres
- generer/coller un token JWT admin
- ajouter un livre
- supprimer un livre

## Obtenir un token JWT
```powershell
$body = '{"username":"admin","password":"admin123"}'
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/auth/token" -ContentType "application/json" -Body $body
```

## Activer le mode Mongo reactive
Configurer `spring.data.mongodb.uri` puis activer le profil `mongo`.
