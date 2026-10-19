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
- [POC](#POC)
  - [Garage](#garage)
  - [Jacoco](#couverture-de-code-avec-jacoco)

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

## POC

### Garage

#### Description

Garage est une solution permettant le stockage de fichiers. 
Garage se base sur le standard S3 ainsi peu de choses sont a implémenter car nous allons reprendre une librairie qui implémente ce standard.
Ainsi nous ne devons nous occuper que de configurer les objets néscéssaire. 

#### Configuration 

Pour configurer garage on utilise le fichier garage.toml. 
Voici le détail de chaque propriétés : 

| Propriété                       | Description                                                                                                              | Valeurs                                                         |
|---------------------------------|--------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| metadata_dir                    | Dossier des métadonnées (index des objets, buckets, clés)                                                                | /var/lib/garage/meta                                            |
| data_dir                        | Dossier ou sont stockés les fichiers                                                                                     | /var/lib/garage/data                                            |
| db_engine                       | Moteur pour les métadonnées                                                                                              | lmdb                                                            |
| metadata_auto_snapshot_interval | Fréquences des sauvegarde des métadonnées                                                                                | 6h                                                              |
| replication_factor              | Nombre de copie des données                                                                                              | 1                                                               |
| compression_level               | Niveau de compression des données                                                                                        | 2                                                               |
| rpc_bind_addr                   | Adresse d'écoute entre les noeuds (inutile ici car pas de réplication)                                                   | [::]:3901"                                                      |
| rpc_public_addr                 | Adresse de communication entre les noeuds                                                                                | 127.0.0.1:3901                                                  |
| rpc_secret                      | Mot de passe permettant au noeuds de communiquer                                                                         | 0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef |
| **s3_api**                      |                                                                                                                          |                                                           |
| [s3_api] s3_region              | Région du serveur, (requis pour le standard S3 mais inutile pour nous, il doit être identique avec le client implémenté) | garage                                                          |
| [s3_api] api_bind_addr          | Port de l'API S3 utilisée par l'application                                                                              |                                                       |
| **admin** Pour gérer Garage     |                                                                                                                          | [::]:3903                                                       |
| [admin] api_bind_addr           | Port sur lequel écoute l'administration                                                                                  | [::]:3903                                                       |
| [admin] admin_token             | Token de l'admin                                                                                                         | dev-admin-token                                                 |


#### Utilisation 

Pour l'intégrer il faut ajouter à la configuration les quatres éléments suivants :

En amont nous avons créer un record qui regroupe les éléments de configuration de garage : 

- GarageProperties
    ```java
    @ConfigurationProperties(prefix = "garage")
    public record GarageProperties(
            String endpoint,
            String accessKey,
            String secretKey,
            String regionName,
            String bucket
    ) {}
    ```
Ensuite on va utiliser ces properties pour créer nos Beans :
 
- StaticCredentialsProvider, cet objet permet d’avoir les droits d’accès à Garage.
  ```java
    private static StaticCredentialsProvider credentials(GarageProperties props) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(props.accessKey(), props.secretKey()));
    }
  ```
- S3Client, permet d’envoyer des requêtes à S3. On doit y renseigner l’url de Garage (endpointOverride), les droits d’accès (credentialsProvider).
  ```java
    @Bean
    S3Client s3Client(GarageProperties props){
        return S3Client
                .builder()
                .endpointOverride(URI.create(props.endpoint()))
                .region(Region.of(props.regionName()))
                .credentialsProvider(credentials(props))
                .forcePathStyle(true)
                .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                .responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED)
                .build();
    }
  ```
- S3Presigner renseigné avec les mêmes infos que le S3Client permet de générer des liens temporaires vers Garage.
  ```java
    @Bean
    S3Presigner s3Presigner(GarageProperties props) {
        return S3Presigner.builder()
                .endpointOverride(URI.create(props.endpoint()))
                .region(Region.of(props.regionName()))
                .credentialsProvider(credentials(props))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
  ```
  
Maintenant que les beans sont créer on va pouvoir créer `GarageRepository` pour dialoguer avec Garage. On peut aussi utiliser S3Presigner pour récupérer des liens à durée de vie limitée.
  ```java
    @Repository
    @RequiredArgsConstructor
    public class GarageRepository{
        private final S3Client s3Client;
        private final S3Presigner s3Presigner;
        private final GarageProperties props;
    
        public void upload(String key, InputStream content, long size, String contentType) {
            s3Client.putObject(
                    r -> r.bucket(props.bucket()).key(key).contentType(contentType),
                    RequestBody.fromInputStream(content, size));
        }
    
        public InputStream download(String key) {
            return s3Client.getObject(r -> r.bucket(props.bucket()).key(key));
        }
    
        public void delete(String key) {
            s3Client.deleteObject(r -> r.bucket(props.bucket()).key(key));
        }
    
        public URL presignedDownloadUrl(String key, Duration validity) {
            return s3Presigner.presignGetObject(r -> r
                            .signatureDuration(validity)
                            .getObjectRequest(g -> g.bucket(props.bucket()).key(key)))
                    .url();
        }

    }

   ```

### RabbitMQ

#### Description

RabbitMQ permet d'envoyer des messages entre différents services. On passera par des messages (en asynchrone) seulement quand un service n'a pas besoin d'attendre la réponse, par exemple pour envoyer une notification.

#### Configuration

- Chaque microservice doit ajouter la dépendance `spring-boot-starter-amqp`.
- Pour créer une queue, on ajoute pour chacune un bean de type `Queue` qui définit son nom.
- Pour envoyer un message, on utilise une instance de `RabbitTemplate` en précisant le nom de la queue souhaitée.
- Pour recevoir un message, on utilise l'annotation `@RabbitListener` en précisant le nom de la queue écoutée. Pour que cela fonctionne, le receveur doit être connecté à RabbitMQ.

#### Utilisation

RabbitMQ propose une interface web pour suivre les différentes queues et y déposer des messages directement. On peut y voir le statut des queues et des consommateurs, ainsi que des graphiques sur les flux de messages. On y voit les messages envoyés puis consommés.

Pour tester : le service Commande envoie des messages dans une queue, le service Users les reçoit et les affiche.

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


