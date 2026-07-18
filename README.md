# CareerConnect — Campus Placement Platform

CareerConnect is a modular Spring Boot backend platform designed for campus placement management and context-aware student career guidance.

---

## 1. Project Overview & Features
- **Student Profile Management**: Enforces data validation, CGPA/backlog tracking, skills mapping, and email uniqueness.
- **Company & Placement Drive Management**: Allows creation of placement drives with granular eligibility rules (minimum CGPA, backlog caps, allowed programmes, target graduation year, and required skill sets).
- **Deterministic Eligibility Evaluation**: Evaluates student profile against drive requirements deterministically using policy strategy classes and returns itemized pass/fail reasons.
- **Application Lifecycle Management**: Supports drive applications, prevents duplicate applications, enforces drive deadlines, and manages strict status state transitions (`SUBMITTED` → `UNDER_REVIEW` → `SHORTLISTED` → `SELECTED`/`REJECTED`).
- **Advisor Integration**: Provides context-aware guidance and placement FAQs using a local assistant service (Ollama `llama3.2`).
- **Resilient Architectural Boundary**: Keeps the advisory functionality purely advisory. If the assistant service is unavailable, all core REST endpoints continue operating normally.

---

## 2. Technology Stack & Requirements
- **Java**: 21 (compatible with Java 17+)
- **Framework**: Spring Boot 3.2.12 (Spring Web, Spring Validation)
- **Persistence**: In-Memory Repositories (`ConcurrentHashMap`)
- **Assistant Engine**: Ollama running locally (`llama3.2`)
- **Build Tool**: Maven Wrapper (`mvnw.cmd` / `./mvnw`) included — No separate Maven installation required

---

## 3. How to Start the Assistant Service & Configure Model

1. **Install Ollama**: Download and install from [ollama.com](https://ollama.com).
2. **Pull the Model**:
   ```bash
   ollama pull llama3.2
   ```
3. **Ensure Service is Running**:
   Ollama typically runs automatically in the background at `http://localhost:11434`.
4. **Configuration in `application.properties`**:
   ```properties
   ollama.base-url=http://localhost:11434
   ollama.model=llama3.2
   ollama.connect-timeout-seconds=3
   ollama.read-timeout-seconds=60
   ```

---

## 4. How to Build and Run the Application

### Option A: Using the Included Maven Wrapper (Recommended)
From the project root directory, run:

```powershell
# Compile and build
.\mvnw.cmd compile

# Run the Spring Boot application
.\mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`.

---

## 5. Sample Initialization & Demonstration Sequence

Execute the following sequence in Postman or cURL to demonstrate end-to-end functionality:

1. **Create Student Profile**:
   `POST /api/students`
   ```json
   {
     "name": "Rahul Verma",
     "email": "rahul@example.com",
     "programme": "B.Tech CS",
     "graduationYear": 2026,
     "cgpa": 8.5,
     "activeBacklogs": 0,
     "skills": ["Java", "Spring Boot", "SQL"]
   }
   ```
   *Output*: Student created with ID `STU-001`.

2. **Create Company**:
   `POST /api/companies`
   ```json
   {
     "name": "Acme Corp",
     "sector": "Information Technology",
     "description": "Enterprise cloud provider"
   }
   ```
   *Output*: Company created with ID `CMP-001`.

3. **Create Placement Drive**:
   `POST /api/drives`
   ```json
   {
     "companyId": "CMP-001",
     "role": "Backend Engineer",
     "location": "Bengaluru",
     "packageLpa": 12.0,
     "deadline": "2026-12-31",
     "requiredSkills": ["Java", "Spring Boot"],
     "minCgpa": 7.5,
     "maxBacklogs": 0,
     "allowedProgrammes": ["B.Tech CS"],
     "graduationYear": 2026
   }
   ```
   *Output*: Drive created with ID `DRV-100`.

4. **Evaluate Eligibility**:
   `GET /api/drives/DRV-100/eligibility/STU-001`
   *Output*: `eligible: true` with reason break-down.

5. **Submit Application**:
   `POST /api/drives/DRV-100/applications` Body: `{"studentId": "STU-001"}`
   *Output*: Application created with ID `APP-500` in `SUBMITTED` status.

6. **Demonstrate Duplicate Prevention**:
   Repeat the same POST call to get a `409 Conflict` error.

7. **Update Application Status**:
   `PATCH /api/applications/APP-500/status` Body: `{"status": "UNDER_REVIEW"}`

8. **Query Advisor Assistant**:
   `POST /api/chat`
   ```json
   {
     "studentId": "STU-001",
     "driveId": "DRV-100",
     "message": "What topics should I revise for this Backend Engineer drive?"
   }
   ```

---

## 6. Endpoint Summary

| Method | Path | Purpose | Key Status Codes |
|--------|------|---------|-----------------|
| `POST` | `/api/students` | Create student | 201, 400, 409 |
| `GET` | `/api/students/{studentId}` | Retrieve student | 200, 404 |
| `PUT` | `/api/students/{studentId}` | Update student profile | 200, 400, 404 |
| `POST` | `/api/companies` | Create company | 201, 400, 409 |
| `GET` | `/api/companies/{companyId}` | Retrieve company | 200, 404 |
| `POST` | `/api/drives` | Create placement drive | 201, 400, 404 |
| `GET` | `/api/drives` | List/filter drives | 200 |
| `GET` | `/api/drives/{driveId}` | Retrieve drive | 200, 404 |
| `GET` | `/api/drives/{driveId}/eligibility/{studentId}` | Evaluate eligibility with reasons | 200, 404 |
| `POST` | `/api/drives/{driveId}/applications` | Submit application | 201, 400, 404, 409 |
| `GET` | `/api/students/{studentId}/applications` | List student applications | 200, 404 |
| `GET` | `/api/applications/{applicationId}` | Retrieve one application | 200, 404 |
| `PATCH` | `/api/applications/{applicationId}/status` | Update application status | 200, 400, 404, 409 |
| `POST` | `/api/chat` | Ask career advisor | 200, 400, 503 |

See `docs/api-contract.md` for full request/response JSON examples.

---

## 7. Design Patterns Summary

1. **Strategy Pattern (`com.careerconnect.policy`)**:
   - `EligibilityPolicy` interface implemented by `CgpaPolicy`, `BacklogPolicy`, `SkillPolicy`, `GraduationYearPolicy`, and orchestrated by `CompositeEligibilityPolicy`.
   - *Rationale*: Allows adding or altering individual criterion evaluation rules without changing drive or application services.

2. **Adapter Pattern (`com.careerconnect.chat`)**:
   - `ChatClient` interface adapted by `OllamaChatClient`.
   - *Rationale*: Hides HTTP protocol details, Jackson parsing, and request shapes behind a clean interface.

3. **Repository Pattern (`com.careerconnect.repository`)**:
   - Interfaces `StudentRepository`, `CompanyRepository`, `DriveRepository`, `ApplicationRepository` with in-memory `ConcurrentHashMap` implementations.
   - *Rationale*: Isolates persistent domain data retrieval logic from business logic.

---

## 8. Known Limitations & Future Work

- **Persistence Scope**: In-memory storage (`ConcurrentHashMap`). Data resets on restart. Adding H2/PostgreSQL with JPA repository implementations is a direct drop-in.
- **Authentication**: No authentication or role-based access control. JWT / Spring Security would be added in production.
- **Asynchronous Processing**: Advisor requests are synchronous with configurable timeout. Could be replaced with `WebClient` or `@Async`.
- **No Unit Tests**: Time constraint of the project. Policy strategies and transition logic are testable as plain Java methods.
- **Single Instance**: Prototype runs as one application instance. Production would add a load balancer and externalized state.
- **No Frontend**: Explicitly out of scope per project constraints. A React/Angular SPA consuming these APIs would be the next step.
