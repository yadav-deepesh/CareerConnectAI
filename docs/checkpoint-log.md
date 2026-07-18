# Stage Checkpoint Log - CareerConnect

## Stage Checkpoints Summary

| Day / Stage | Description | Key Deliverable | Status |
|-------------|-------------|-----------------|--------|
| **Day 1 - D1-A** | Problem Analysis & Scope definition | Scope, actors, core assumptions documented | **Completed** |
| **Day 1 - D1-B** | Domain Modeling & Responsibilities | CRC responsibility matrix & class entities | **Completed** |
| **Day 1 - D1-C & D1-D** | Core Entities & Storage Layer | Student, Company, Drive, Application entities + ConcurrentHashMap repositories | **Completed** |
| **Day 2 - D2-A** | Eligibility Strategy Pattern | `EligibilityPolicy` interface & implementations (`CgpaPolicy`, `BacklogPolicy`, `SkillPolicy`, `GraduationYearPolicy`, `CompositeEligibilityPolicy`) | **Completed** |
| **Day 2 - D2-B** | Application State Lifecycle | Enforced status transitions in `ApplicationStatus` & duplicate application guard | **Completed** |
| **Day 2 - D2-C** | Class diagram & responsibility table | LLD artefacts created | **Completed** |
| **Day 2 - D2-D** | Two sequence diagrams | Apply-to-drive and chatbot sequences | **Completed** |
| **Day 3 - D3-A & D3-B** | REST API Contract & HLD | Resource-oriented REST contract & basic HLD architecture diagram | **Completed** |
| **Day 3 - D3-C & D3-D** | Controllers, DTOs & Validation | Controller endpoints with Bean Validation (`@Valid`) & `GlobalExceptionHandler` | **Completed** |
| **Day 4 - D4-A & D4-B** | Chat Client Adapter | `ChatClient` interface & `OllamaChatClient` adapter with HTTP timeout error mapping | **Completed** |
| **Day 4 - D4-C & D4-D** | Assistant Service & Graceful Degradation | `CareerAssistantService` with context builder & 503 handling when advisor service is down | **Completed** |
| **Day 5 - D5-A to D5-D** | End-to-End Build & Test Package | Clean Maven build, Postman test suite (T-01 to T-17) & documentation package | **Completed** |

---

## Use-Case 1: Apply to Placement Drive

| Field | Detail |
|-------|--------|
| **Actor** | Student |
| **Preconditions** | Student profile exists. Company and Placement Drive exist. Drive deadline has not passed. |
| **Normal Flow** | 1. Student sends `POST /api/drives/{driveId}/applications` with their `studentId`. 2. System verifies student exists (`StudentRepository`). 3. System verifies drive exists (`DriveRepository`). 4. System checks drive deadline has not passed (`PlacementDrive.isDeadlinePassed()`). 5. System checks no duplicate application exists (`ApplicationRepository.findByDriveIdAndStudentId()`). 6. System evaluates eligibility via `CompositeEligibilityPolicy.evaluate()`. 7. If eligible, system creates `Application` with status `SUBMITTED` and returns `201 Created`. |
| **Alternate Flow A** | Student is ineligible → system returns `400 Bad Request` with itemized reasons from each policy. |
| **Alternate Flow B** | Student already applied → system returns `409 Conflict` with duplicate error message. |
| **Alternate Flow C** | Drive deadline passed → system returns `400 Bad Request` with deadline error. |
| **Alternate Flow D** | Student or Drive ID not found → system returns `404 Not Found`. |

## Use-Case 2: Ask Career Advisor

| Field | Detail |
|-------|--------|
| **Actor** | Student |
| **Preconditions** | Advisor service running locally. Model (`llama3.2`) pulled and available. |
| **Normal Flow** | 1. Student sends `POST /api/chat` with optional `studentId`, optional `driveId`, and a `message`. 2. `CareerAssistantService` fetches student profile from repository (if provided). 3. Service fetches drive details from repository (if provided). 4. If both exist, service runs deterministic eligibility evaluation and includes the result in context. 5. Service builds a context-aware prompt with the system advisory prompt and verified data. 6. Service calls `ChatClient.chat()` which delegates to `OllamaChatClient`. 7. Adapter sends HTTP POST to Ollama `/api/chat` endpoint with `{"model":..., "messages":..., "stream": false}`. 8. Adapter parses response JSON, extracts `message.content`, returns to service. 9. Controller wraps answer in `ChatResponse` with `advisory: true` and returns `200 OK`. |
| **Alternate Flow A** | Service not running → `ResourceAccessException` caught → `ServiceUnavailableException` → `503 Service Unavailable`. All other placement APIs continue working. |
| **Alternate Flow B** | Service timeout → same 503 handling as above. |
| **Alternate Flow C** | Empty response → safe fallback message returned ("The assistant could not generate a response at this time"). |
| **Alternate Flow D** | Student or Drive not found → context is simply omitted, general FAQ mode proceeds. |

---

## Responsibility Table (CRC — Class, Responsibility, Collaborators)

| # | Class / Interface | Responsibility | Collaborators |
|---|-------------------|---------------|---------------|
| 1 | `Student` | Own student profile state (name, email, programme, CGPA, skills). Provide controlled update method. Defensive copy on skills list. | — |
| 2 | `Company` | Represent an organization participating in placements (name, sector, description). | — |
| 3 | `PlacementDrive` | Represent role details, deadline, required skills, eligibility criteria. Report whether deadline has passed. | `DriveStatus` |
| 4 | `Application` | Represent one student-drive application. Validate and execute status transitions through `transitionTo()`. | `ApplicationStatus` |
| 5 | `ApplicationStatus` | Enumerate the 6 lifecycle states. Define allowed transitions via `VALID_TRANSITIONS` map. | — |
| 6 | `EligibilityResult` | Immutable value object holding boolean eligible flag and list of reason strings. | — |
| 7 | `EligibilityPolicy` | Strategy interface defining `evaluate(Student, PlacementDrive)` contract. | `Student`, `PlacementDrive` |
| 8 | `CompositeEligibilityPolicy` | Run all individual policy strategies and aggregate pass/fail reasons. | `CgpaPolicy`, `BacklogPolicy`, `SkillPolicy`, `GraduationYearPolicy` |
| 9 | `ChatClient` | Adapter target interface for communication (`chat(systemPrompt, userMessage)`). | — |
| 10 | `OllamaChatClient` | Adapter implementation. Translate to Ollama HTTP format, handle timeouts, parse JSON response. | `RestTemplate`, `ObjectMapper` |
| 11 | `StudentService` | Coordinate student creation (duplicate email check), retrieval, and profile updates. | `StudentRepository` |
| 12 | `CompanyService` | Coordinate company creation (duplicate name check) and retrieval. | `CompanyRepository` |
| 13 | `DriveService` | Coordinate drive creation (verify company exists, validate deadline), listing, filtering, eligibility evaluation. | `DriveRepository`, `CompanyRepository`, `StudentRepository`, `EligibilityPolicy` |
| 14 | `ApplicationService` | Coordinate the 5-step application guard (exists → deadline → duplicate → eligibility → create). Manage status transitions. | `ApplicationRepository`, `StudentRepository`, `DriveRepository`, `EligibilityPolicy` |
| 15 | `CareerAssistantService` | Retrieve verified context from repos, build safe prompts, delegate to `ChatClient`. | `ChatClient`, `StudentRepository`, `DriveRepository`, `EligibilityPolicy` |
| 16 | `GlobalExceptionHandler` | Catch all controller exceptions and map to consistent JSON error responses with correct HTTP status codes. | All custom exceptions |

---

## Application Status Lifecycle (State Model)

```
                    ┌──────────────┐
                    │  SUBMITTED   │
                    └──────┬───────┘
                           │
              ┌────────────┼────────────┐
               │            │            │
               ▼            │            ▼
        ┌──────────┐       │     ┌────────────┐
        │ WITHDRAWN │       │     │            │
        └──────────┘       │     │            │
                           ▼     │            │
                    ┌──────────────┐         │
                    │ UNDER_REVIEW │         │
                    └──────┬───────┘         │
                           │                 │
              ┌────────────┼────────────┐    │
              │            │            │    │
              ▼            ▼            ▼    │
       ┌───────────┐ ┌──────────┐ ┌──────────┘
       │ SHORTLISTED│ │ REJECTED │ │ WITHDRAWN │
       └─────┬─────┘ └──────────┘ └───────────┘
             │
        ┌────┴────┐
        │         │
        ▼         ▼
 ┌──────────┐ ┌──────────┐
 │ SELECTED │ │ REJECTED │
 └──────────┘ └──────────┘
```

**Allowed transitions (from `ApplicationStatus.java`):**

| From | Allowed Next States |
|------|-------------------|
| `SUBMITTED` | `UNDER_REVIEW`, `WITHDRAWN` |
| `UNDER_REVIEW` | `SHORTLISTED`, `REJECTED`, `WITHDRAWN` |
| `SHORTLISTED` | `SELECTED`, `REJECTED` |
| `SELECTED` | *(terminal — no further transitions)* |
| `REJECTED` | *(terminal — no further transitions)* |
| `WITHDRAWN` | *(terminal — no further transitions)* |

**Invariant**: Any transition not in this table is rejected by `Application.transitionTo()` → throws `InvalidTransitionException` → HTTP `409 Conflict`.

---

## Pattern Implementation Evidence

### 1. Strategy Pattern — Eligibility Evaluation
- **Interface**: `com.careerconnect.policy.EligibilityPolicy`
- **Concrete Strategies**:
  - `CgpaPolicy` — checks `student.getCgpa() >= drive.getMinCgpa()`
  - `BacklogPolicy` — checks `student.getActiveBacklogs() <= drive.getMaxBacklogs()`
  - `SkillPolicy` — checks if student has all required skills (case-insensitive)
  - `GraduationYearPolicy` — checks graduation year match
- **Composite**: `CompositeEligibilityPolicy` — iterates all four and aggregates all reasons
- **Variation point**: New eligibility criteria can be added as a new policy class without modifying any service code.

### 2. Adapter Pattern — Advisor Integration
- **Target Interface**: `com.careerconnect.chat.ChatClient`
- **Adapter Implementation**: `com.careerconnect.chat.OllamaChatClient`
- **What it adapts**: Translates `chat(systemPrompt, userMessage)` into Ollama's HTTP POST format (`/api/chat` with `{"model":..., "messages":..., "stream": false}`), handles JSON response parsing, and maps `ResourceAccessException` → `ServiceUnavailableException`.
- **Variation point**: Swapping to a different provider requires only a new `ChatClient` implementation.

### 3. Repository Pattern — Data Abstraction
- **Interfaces**: `StudentRepository`, `CompanyRepository`, `DriveRepository`, `ApplicationRepository`
- **In-Memory Implementations**: `InMemoryStudentRepository`, `InMemoryCompanyRepository`, `InMemoryDriveRepository`, `InMemoryApplicationRepository` (all using `ConcurrentHashMap`)
- **Variation point**: Replacing in-memory storage with JPA/H2 requires only new repository implementations; service code remains untouched.

---

## Key Invariants Protected in Code
- Student email must be unique → checked in `StudentService.createStudent()`.
- A placement drive must reference an existing company → checked in `DriveService.createDrive()`.
- Drive deadline cannot be in the past at creation → checked in `DriveService.createDrive()`.
- A student cannot submit two applications to the same drive → checked via `ApplicationRepository.findByDriveIdAndStudentId()`.
- Only eligible students may submit an application → enforced by `EligibilityPolicy.evaluate()` in `ApplicationService`.
- Application status may change only through allowed transitions → enforced by `ApplicationStatus.canTransitionTo()`.
- The advisor assistant cannot update entities or invent database state → system prompt explicitly says advisory only; no write operations exposed to `CareerAssistantService`.
