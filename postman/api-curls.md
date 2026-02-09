# Adventure Book API – cURL commands

Base URL: `http://localhost:8080`

## Books

### List all books
```bash
curl -X GET "http://localhost:8080/api/books"
```

### List books (filter by title)
```bash
curl -X GET "http://localhost:8080/api/books?title=Crystal"
```

### List books (filter by author)
```bash
curl -X GET "http://localhost:8080/api/books?author=Evelyn"
```

### List books (filter by difficulty)
```bash
curl -X GET "http://localhost:8080/api/books?difficulty=EASY"
```

### List books (filter by category)
```bash
curl -X GET "http://localhost:8080/api/books?category=ADVENTURE"
```

### Get book by id
```bash
curl -X GET "http://localhost:8080/api/books/1"
```

### Update book categories
```bash
curl -X PATCH "http://localhost:8080/api/books/1/categories" \
  -H "Content-Type: application/json" \
  -d "{\"categories\":[\"ADVENTURE\",\"FICTION\"]}"
```

### Get section
```bash
curl -X GET "http://localhost:8080/api/books/1/sections/1"
```

---

## Sessions

### Create session
```bash
curl -X POST "http://localhost:8080/api/sessions" \
  -H "Content-Type: application/json" \
  -d "{\"bookId\":1}"
```

### Get session (replace 1 with session id from create response)
```bash
curl -X GET "http://localhost:8080/api/sessions/1"
```

### Get current section
```bash
curl -X GET "http://localhost:8080/api/sessions/1/current"
```

### Make choice (replace 1 with session id, 2 with gotoSectionId from options)
```bash
curl -X POST "http://localhost:8080/api/sessions/1/choices" \
  -H "Content-Type: application/json" \
  -d "{\"gotoSectionId\":2}"
```

---

## Import in Postman

1. **From collection file:** File → Import → Upload `Adventure-Book-API.postman_collection.json`
2. **From cURL:** Import → Raw text → paste any curl above → Continue → Import

Collection variables: `baseUrl` (http://localhost:8080), `bookId` (1), `sessionId` (updated automatically after "Create session" if you use the collection).
