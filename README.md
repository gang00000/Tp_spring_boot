# Projet TP Spring Boot 🚀

Application web Spring Boot réalisée dans le cadre de travaux pratiques.

## Fonctionnalités

### TP1 — Gestion des Produits
- ✅ Lister, ajouter, modifier, supprimer des produits
- ✅ API REST complète (`GET`, `POST`, `PUT`, `DELETE`) sur `/api/products`
- ✅ Validation des données côté serveur (`@NotBlank`, `@Positive`)

### TP2 — Blog avec Commentaires
- ✅ Lister, publier, supprimer des articles
- ✅ Ajouter des commentaires sur un article
- ✅ Validation des formulaires avec affichage des erreurs

## Sécurité
- Spring Security avec authentification par formulaire
- Encodage des mots de passe avec **BCrypt**
- Deux rôles : `USER` et `ADMIN`

## Comptes de test

| Username | Password | Rôle |
|----------|----------|------|
| `user`   | `password` | USER |
| `admin`  | `password` | ADMIN |

## Stack Technique

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Security | 6 |
| Spring Data JPA | — |
| Thymeleaf | — |
| H2 Database (dev) | — |
| MySQL (prod) | — |
| Lombok | — |

## Lancer l'application

```bash
mvn spring-boot:run
```

L'application démarre sur : [http://localhost:8080](http://localhost:8080)

La console H2 est disponible sur : [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## Tests

```bash
mvn test
```

14 tests unitaires couvrant les services `ProductService` et `BlogService`.

## Architecture

```
src/
├── main/
│   ├── java/com/example/tp/
│   │   ├── config/          # SecurityConfig, GlobalExceptionHandler
│   │   ├── controller/      # MainController, ProductRestController
│   │   ├── model/           # Product, Article, Comment
│   │   ├── repository/      # JPA Repositories
│   │   └── service/         # ProductService, BlogService
│   └── resources/
│       ├── templates/       # Thymeleaf HTML
│       └── application.yml  # Configuration multi-profils
└── test/                    # Tests JUnit 5 + Mockito
```
