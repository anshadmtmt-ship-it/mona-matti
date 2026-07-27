# MONA MATTI — Architecture Documentation

## 1. Executive Summary
MONA MATTI is an enterprise Spring Boot application designed as a continuous architect's notebook sheet for luxury stationery showcase and reservation tracking.

## 2. High-Level Architecture Pattern
The project strictly enforces Layered Architecture & MVC Separation of Concerns:
- **Presentation Layer**: Spring MVC Controllers (`HomeController`, `AdminController`, `ReservationController`) returning Thymeleaf views and REST JSON endpoints.
- **Service Layer**: Interface contracts (`ProductService`, `ReservationService`) with `@Transactional` implementations (`ProductServiceImpl`, `ReservationServiceImpl`).
- **Data Access Layer**: Spring Data JPA repositories (`ProductRepository`, `ReservationRepository`).
- **Persistence Layer**: Relational Database (H2 file-based in `dev`, MySQL 8 in `prod`).

```
[ Client Browser ]
       │
       ▼ (HTTP GET / POST)
[ Spring Security / DispatcherServlet ]
       │
       ├──► HomeController (Public View Engine)
       ├──► AdminController (CMS Management View)
       └──► ReservationController (POST /reserve REST API)
               │
               ▼
       [ Service Layer Interfaces ]
               │
               ▼
       [ Spring Data JPA Repositories ]
               │
               ▼
       [ Database (H2 / MySQL) ]
```

## 3. Technology Stack & Rationale
- **Java 21**: Modern LTS with record types, virtual threads readiness, and superior JVM performance.
- **Spring Boot 3.2.2**: Enterprise web framework providing auto-configuration, HikariCP connection pooling, and Actuator monitoring.
- **Thymeleaf**: Server-side HTML5 template engine supporting zero-downtime HTML rendering.
- **Spring Data JPA & Hibernate 6**: Object-Relational Mapping with automated schema management and transaction control.
- **HikariCP**: High-performance JDBC connection pool configured with production bounds (`min-idle: 5`, `maximum-pool-size: 20`).
- **Vanilla CSS & JS**: Zero external framework bloat for maximum GPU-accelerated rendering performance (`60 FPS`).

## 4. Key Architectural Decisions (ADRs)
1. **OSIV Disabled (`spring.jpa.open-in-view=false`)**: Prevents long-lived database connections during template rendering, avoiding thread pool exhaustion under heavy traffic load.
2. **Constructor Injection**: 100% immutable bean injection across all components for superior testability and lifecycle safety.
3. **Global Exception Handling**: Centralized [`GlobalExceptionHandler`](file:///home/anshad/project/src/main/java/com/monamatti/exception/GlobalExceptionHandler.java) catches exceptions silently without leaking sensitive stack traces.
