# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Security learning/demo project ("EazyBank" backend). Spring Boot 4.1.x, Java 25, Maven. Base package `de.jdtbee.dev`, entry point `EazyBankBackendApplication`.

## Commands

The `.mvn/` wrapper directory is not checked in, so `./mvnw` will not work — use the system `mvn`.

```bash
mvn spring-boot:run                                   # run app on http://localhost:8088
mvn clean package                                     # build jar (runs tests)
mvn test                                              # all tests
mvn test -Dtest=EazyBankBackendApplicationTests       # single test class
mvn test -Dtest=EazyBankBackendApplicationTests#contextLoads   # single test method
```

## Architecture / configuration notes

- Configuration lives in `src/main/resources/application.yaml` (the old `application.properties` was removed). Values use `${ENV_VAR:default}` placeholders: `SPRING_APP_NAME`, `SECURITY_USERNAME` (default `admin`), `SECURITY_PASSWORD` (default `admin123`), `LOGPATTERN_CONSOLE`.
- Security is defined in `config/ProjectSecurityConfig` (`SecurityFilterChain` bean): the H2 console is public (CSRF ignored, `X-Frame-Options: SAMEORIGIN` so its frames render); every other endpoint requires form login or HTTP Basic with the single in-memory user from `spring.security.user.*`. Use `PathRequest.toH2Console()` from `org.springframework.boot.security.autoconfigure.web.servlet` (Boot 4 package).
- Persistence: in-memory H2 (`jdbc:h2:mem:testdb`, user `sa`, empty password) with H2 console enabled (`/h2-console`). `spring.jpa.hibernate.ddl-auto` is `validate`, so any JPA entity added must have a matching table created by a `schema.sql` (or similar) — Hibernate will not create it and startup will fail otherwise.
- springdoc-openapi is on the classpath (Swagger UI at `/swagger-ui.html`); it is also behind Spring Security unless explicitly permitted.
- Controllers go in `de.jdtbee.dev.controller`. Lombok is configured as an annotation processor for both main and test compilation.
