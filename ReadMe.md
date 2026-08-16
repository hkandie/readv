# ReadV

Monorepo containing a React frontend (`frontend/`) and a Spring Boot backend (`backend/`).

## Backend

```bash
cd backend

# Run (local profile, trusts Windows Root certs)
./gradlew bootRunLocal

# Run with a different profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Test
./gradlew test
```

Available profiles: `local`, `dev`, `qa`, `uat`, `prod`.

## Frontend

```bash
cd frontend

npm install

# Run
npm run dev

# Test
npm test
```
