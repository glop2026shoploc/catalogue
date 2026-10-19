# catalogue

Microservice **catalogue produits** du projet glop — Spring Boot (Java 21) + PostgreSQL.

| | Valeur |
|---|---|
| API en local | http://localhost:8085 |
| Base de dev | `localhost:5436` — base `catalogue`, utilisateur `catalogue`, mot de passe `catalogue` |
| Image Docker | `ghcr.io/glop2026shoploc/catalogue` |

---

## Sommaire

- [Prérequis](#prérequis)
- [Démarrage rapide](#démarrage-rapide)
- [Lancer l'application en local](#lancer-lapplication-en-local)
- [Arrêter](#arrêter)
- [Base de données](#base-de-données)
- [Configuration](#configuration)
- [Tests et build](#tests-et-build)
- [Docker et CI](#docker-et-ci)
- [Avec les autres microservices](#avec-les-autres-microservices)
- [Conventions](#conventions)

---

## Eureka

URL : [http://localhost:8761/](<http://localhost:8761/>)

Eureka regroupe les routes et centralise les points d’accès aux différents microservices de l’application.

Pour l’utiliser, lancer d’abord le projet **Eureka Server** depuis son repository, puis démarrer l’application.

## Prérequis

| Outil | Version | Vérifier |
|---|---|---|
| Java (JDK) | 21 | `java -version` |
| Docker (Docker Desktop), **lancé** | récent | `docker --version` |
| Git | — | `git --version` |

Maven n'a pas besoin d'être installé : utiliser `./mvnw` (ou `mvnw.cmd` sous Windows).

---

## Démarrage rapide

```bash
git clone https://github.com/glop2026shoploc/catalogue.git
cd catalogue
# Pas besoin de docker compose up -d qui démarre PostgreSQL car nous avons la dépendance spring compose
./mvnw spring-boot:run      # démarre l'application
```

→ http://localhost:8085

---

## Lancer l'application en local

L'**application** tourne sur ta machine (IDE ou Maven) et la **base** tourne dans Docker.

### 1. Démarrer la base de données

```bash
docker compose up -d
```

**Ce que ça fait :** lit `compose.yaml` et lance un conteneur PostgreSQL 17 sur `localhost:5436`. Au premier lancement, la base `catalogue` et l'utilisateur sont créés automatiquement ; les données sont conservées dans un volume Docker.

> Si la dépendance `spring-boot-docker-compose` est présente, cette étape est **automatique** au lancement de l'app.

### 2. Démarrer l'application

**IntelliJ :** ouvrir le projet, lancer la classe `*Application` (Run ou Debug).

**Terminal :**

```bash
./mvnw spring-boot:run
```

**Ce que ça fait :** compile le code et démarre Spring Boot sur le port **8085**. Au démarrage, l'application se connecte à la base et **Flyway** applique les migrations SQL manquantes (`src/main/resources/db/migration`).

### 3. Vérifier

- http://localhost:8085
- http://localhost:8085/actuator/health (si Actuator est installé)

---

## Arrêter

| Commande | Effet |
|---|---|
| Stop dans IntelliJ ou `Ctrl+C` | arrête l'application |
| `docker compose stop` | arrête la base (données conservées) |
| `docker compose down` | supprime le conteneur de la base (données conservées) |
| `docker compose down -v` | ⚠️ supprime aussi les données : base vide au prochain lancement |

---

## Base de données

| Paramètre | Valeur (dev) |
|---|---|
| Hôte | `localhost` |
| Port | `5436` |
| Base | `catalogue` |
| Utilisateur / mot de passe | `catalogue` / `catalogue` |
| URL JDBC | `jdbc:postgresql://localhost:5436/catalogue` |

Console SQL :

```bash
docker compose exec catalogue-db psql -U catalogue -d catalogue
```

---

## Configuration

Fichier `src/main/resources/application.yml` (ou `.properties`). Les valeurs sont écrites ainsi :

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5436/catalogue}
    username: ${SPRING_DATASOURCE_USERNAME:catalogue}
    password: ${SPRING_DATASOURCE_PASSWORD:catalogue}
server:
  port: 8085
```

`${VARIABLE:valeur}` = « la variable d'environnement si elle existe, sinon la valeur par défaut ».

| Où tourne l'app | Valeurs utilisées |
|---|---|
| IDE / `./mvnw` | valeurs par défaut → base du `compose.yaml` |
| Docker (glop-infra, prod) | variables `SPRING_DATASOURCE_*` et `SERVER_PORT=8080` définies dans `glop-infra/catalogue/docker-compose.yml` |

Le code ne change jamais d'un environnement à l'autre : seules les variables changent.

---

## Tests et build

| Commande | Ce que ça fait |
|---|---|
| `./mvnw test` | lance les tests |
| `./mvnw clean package` | compile + tests + crée le jar dans `target/` |
| `./mvnw clean package -DskipTests` | crée le jar sans lancer les tests |

Les tests d'intégration (`@SpringBootTest`) ont besoin d'une base PostgreSQL :
- **avec Testcontainers** : un PostgreSQL temporaire est lancé automatiquement (Docker doit tourner) ;


---

## Docker et CI

### Dockerfile

Le `Dockerfile` **ne compile pas** : il copie le jar construit par `mvn package` dans une image Java 21 et le lance avec `java -jar`.

Construire l'image sur ta machine :

```bash
./mvnw clean package -DskipTests
docker build -t ghcr.io/glop2026shoploc/catalogue:local .
```

### CI (GitHub Actions)

`.github/workflows/ci.yml` appelle le workflow commun du repo `glop2026shoploc/.github`.

| Événement | Tests | Image construite | Image publiée sur GHCR |
|---|---|---|---|
| Pull request vers `main` | ✅ | ✅ | ❌ |
| Merge / push sur `main` | ✅ | ✅ | ✅ `sha-xxxxxxx` + `latest` |
| Tag `v1.2.0` | ✅ | ✅ | ✅ `1.2.0` |

Le check **`ci / build-and-push`** doit être vert pour pouvoir merger.

Publier une version :

```bash
git tag v1.0.0
git push origin v1.0.0
```

Images : <https://github.com/orgs/glop2026shoploc/packages>

---

## Avec les autres microservices

Les autres services se lancent depuis **glop-infra** (images de la CI, version `main`) :

```bash
cd ../glop-infra
./up-local.sh                      # tout le projet
./down-local.sh catalogue                # retirer la version CI de catalogue…
cd ../catalogue && docker compose up -d  # …et lancer ta version depuis l'IDE
```

Ton service (IDE, port 8085) appelle les autres via `localhost:<port>` (voir le README de glop-infra pour la liste des ports).

| Je veux… | Où |
|---|---|
| coder et tester `catalogue` | ce repo (`compose.yaml` + IDE) |
| faire tourner tout le projet | `glop-infra` → `./up-local.sh` |


## Conventions

### Branches

`main` est protégée : travailler sur une branche (`feature/...`, `fix/...`) puis ouvrir une pull request.

### Commits ([Conventional Commits](https://www.conventionalcommits.org/fr/))

| Type | Usage |
|---|---|
| `feat` | nouvelle fonctionnalité |
| `fix` | correction de bug |
| `refactor` | refonte sans changement de comportement |
| `test` | tests |
| `build` | Dockerfile, `pom.xml` |
| `ci` | workflows GitHub Actions |
| `chore` | outillage, config de dev |
| `docs` | documentation |

Exemple : `feat: add product search endpoint`

### Couverture de code avec JaCoCo

JaCoCo mesure les parties du code exécutées pendant les tests.

#### 1. Lancer les tests

Sous Windows, dans PowerShell :

```powershell
.\mvnw.cmd test
```

Sous Linux ou macOS :

```bash
./mvnw test
```

Ou, si Maven est installé :

```bash
mvn test
```

Le rapport est généré automatiquement si JaCoCo est configuré dans le `pom.xml`.

#### 2. Ouvrir le rapport

Le fichier HTML se trouve ici :

```text
target/site/jacoco/index.html
```

On peut l’ouvrir directement avec son navigateur. ou commande :

**Sous Windows (PowerShell) :**

```powershell
Start-Process .\target\site\jacoco\index.html
```

**Sous Linux :**

```bash
xdg-open target/site/jacoco/index.html
```

**Sous macOS :**

```bash
open target/site/jacoco/index.html
```

Le rapport présente les classes, les lignes couvertes ou non couvertes et les pourcentages de couverture.


