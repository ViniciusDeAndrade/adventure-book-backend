# Adventure Book Backend

REST API for adventure books (Spring Boot, Java 17). Implements Objectives 1–4: list/search books, book details and categories, read sections, game sessions with health consequences.

## Build

```bash
mvn clean install
```

## Run

```bash
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar target/adventure-book-backend-1.0.0-SNAPSHOT.jar
```

Default: H2 in-memory, port 8080.

## Use another database (WIP)

To use PostgreSQL (or another DB), add the driver to `pom.xml` and run with:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Set `DB_USER` and `DB_PASSWORD` if needed. Example `application-postgres.yml` is provided.

## API

- **Swagger UI**: http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs  

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | /api/books | List books (optional: title, author, category, difficulty) |
| GET | /api/books/{id} | Book details |
| PATCH | /api/books/{id}/categories | Replace categories (body: `{"categories":["ADVENTURE","FICTION"]}`) |
| GET | /api/books/{bookId}/sections/{sectionId} | Read a section |
| POST | /api/sessions | Start game (body: `{"bookId":1}`) |
| GET | /api/sessions/{id} | Session state (health, current section) |
| GET | /api/sessions/{id}/current | Current section and options |
| POST | /api/sessions/{id}/choices | Make choice (body: `{"gotoSectionId":20}`) |

## Tests

```bash
mvn test
```

Unit tests: `BookValidationServiceTest`, `BookServiceTest`, `GameSessionServiceTest`.  
Integration tests: `BookControllerIntegrationTest`, `SessionControllerIntegrationTest`.

## Resources

- Sample books (JSON): `src/main/resources/books/`
- Exercise PDF: `src/main/resources/Adventure Book Application.pdf`

Only books that pass validation (exactly one BEGIN, at least one END, valid section references, options on non-ending sections) are loaded at startup.

## TODO
- User the Book Validation Service
- User Problem Details in RestControllerAdvice
- set environment to run DB
- HATEOAS?
- docker-compose to start the application?