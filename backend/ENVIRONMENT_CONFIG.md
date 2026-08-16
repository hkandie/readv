# Environment Configuration

This document explains how to use the environment-specific configuration files for the eAdvantage backend application.

## Configuration Files

The application uses Spring Boot's profile-based configuration system. Configuration files are located in `src/main/resources/`:

- **application.yml** - Base/shared configuration (common to all environments)
- **application-local.yml** - Local development configuration
- **application-dev.yml** - Development environment configuration
- **application-qa.yml** - QA environment configuration
- **application-uat.yml** - UAT environment configuration
- **application-prod.yml** - Production environment configuration

## Activating a Profile

### Via Command Line
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Via Environment Variable
```bash
export SPRING_PROFILES_ACTIVE=local
./gradlew bootRun
```

### Via application.properties or application.yml
Add to the base `application.yml`:
```yaml
spring:
  profiles:
    active: local
```

## Environment-Specific Settings

### Local (local)
- **Port**: 8080
- **CORS Origins**: http://localhost:5173, http://localhost:3000
- **Logging Level**: DEBUG for application, INFO for Spring
- **Security Debug**: Enabled
- **Management Endpoints**: health, info, metrics (extended exposure)
- **Use Case**: Local development with detailed logging

### Development (dev)
- **Port**: 8080
- **CORS Origins**: https://dev.example.com
- **Logging Level**: DEBUG for application, INFO for Spring
- **Stacktrace**: Always included
- **Management Endpoints**: health, info, metrics, loggers
- **Use Case**: Development environment with full debugging capability

### QA (qa)
- **Port**: 8080
- **CORS Origins**: https://qa.example.com
- **Logging Level**: INFO for application, WARN for Spring
- **Stacktrace**: Only on request (via ?trace=true)
- **Management Endpoints**: health, info, metrics
- **Use Case**: QA testing with balanced logging

### UAT (uat)
- **Port**: 8080
- **CORS Origins**: https://uat.example.com
- **Logging Level**: WARN for root, INFO for application
- **Stacktrace**: Never included
- **Management Endpoints**: health, info (limited exposure)
- **Error Details**: Limited (no binding errors)
- **Use Case**: User acceptance testing, production-like environment

### Production (prod)
- **Port**: 8080
- **CORS Origins**: https://example.com
- **Logging Level**: WARN for all
- **Stacktrace**: Never included
- **Management Endpoints**: health, info only
- **Error Details**: Minimal (no stacktraces, no binding errors)
- **SSL/TLS**: Enabled (requires certificates)
- **Use Case**: Production environment with minimal information exposure

## Key Differences Summary

| Feature | Local | Dev | QA | UAT | Prod |
|---------|-------|-----|----|----|------|
| Logging Level | DEBUG | DEBUG | INFO | WARN | WARN |
| Stacktraces | Always | Always | On-param | Never | Never |
| Metrics Endpoint | Yes | Yes | Yes | No | No |
| Loggers Endpoint | No | Yes | No | No | No |
| Health Details | Always | Always | When Authorized | When Authorized | When Authorized |
| Error Messages | Full | Full | Full | Limited | None |

## Running with Different Profiles

### Local Development
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Docker (example)
```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/eadvantage.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
```

## Environment Variables for Production

For production environments, sensitive configuration can be set via environment variables:

```bash
export SSL_KEYSTORE_PATH=/path/to/keystore.p12
export SSL_KEYSTORE_PASSWORD=your-keystore-password
export SPRING_PROFILES_ACTIVE=prod
```

## Adding New Configuration Properties

1. Add to base `application.yml` if common to all environments
2. Add to environment-specific files (e.g., `application-prod.yml`) if environment-specific
3. Environment-specific values override base configuration values
4. Use environment variables for sensitive data (e.g., passwords, API keys)
