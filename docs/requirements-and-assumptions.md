# CareerConnect - Requirements and Assumptions

## 1. Project Objectives
- Model a non-trivial campus placement domain using encapsulation, abstraction, and polymorphism.
- Apply at least two meaningful design patterns to real variation points in the code:
  - **Strategy Pattern** for configurable student eligibility evaluation.
  - **Adapter Pattern** for encapsulating advisor assistant runtime calls.
  - **Repository Pattern** for abstracting data persistence.
- Create an implementation-ready LLD with class responsibilities and interaction flows.
- Create a basic HLD showing clients, backend components, storage, and assistant service integration.
- Design resource-oriented REST endpoints with suitable methods, status codes, DTOs, validation, and error responses.
- Integrate a local service through a dedicated abstraction without coupling business logic directly to the external provider.
- Demonstrate testing, documentation, traceability, and incremental checkpoint completion.

## 2. Primary Actors

| Actor | Main Responsibilities |
|-------|----------------------|
| **Student** | Maintain profile, browse drives, check eligibility, apply, track application status, ask the advisor assistant for guidance. |
| **Placement Coordinator / Admin** | Create companies and drives, define eligibility criteria, review applications, and update application status through the pipeline. |
| **Career Advisor Assistant** | Answer supported questions using system-provided context and response generation. |
| **External Runtime Service** | Service that accepts messages and returns generated text. |

## 3. In-Scope Modules
- **Student Profile Management**: Basic personal, academic, and skill information using synthetic data.
- **Company Management**: Company identity, sector, description, and role-related information.
- **Placement Drive Management**: Role, package, deadline, required skills, and academic eligibility rules.
- **Eligibility Evaluation**: Determine and explain whether a student satisfies drive criteria using deterministic logic.
- **Application Management**: Apply, prevent duplicates, retrieve history, and update status through allowed transitions.
- **REST API Layer**: DTOs, validation, exception handling, meaningful status codes, and documented examples.
- **Career Advisor Assistant**: FAQ, eligibility explanation, preparation guidance, and profile summary.

## 4. Out-of-Scope Features
- Production authentication, authorization, JWT, or OAuth.
- Real student records or sensitive personal data.
- JPA/Hibernate or mandatory database deployment.
- Frontend or mobile application.
- Email, SMS, or WhatsApp delivery.
- RAG pipelines, vector databases, model fine-tuning, or agent frameworks.
- Cloud deployment, microservice deployment, Kafka, service discovery, or distributed locks.
- Automated resume scoring or autonomous placement decisions.

## 5. Core Domain Assumptions
1. All data used for demonstration is fictional / synthetic.
2. One Spring Boot application instance is sufficient for the working prototype.
3. Repositories use in-memory collections (`ConcurrentHashMap`); data resets on restart.
4. A placement drive contains one eligibility policy configuration (evaluated by composite strategy).
5. A student may apply only once to a particular drive.
6. The advisor assistant is advisory only — it cannot create, approve, reject, or modify applications.
7. External helper service is installed separately and runs on the target machine at `http://localhost:11434`.
8. The model name and base URL are configurable via `application.properties` and not hard-coded throughout the application.
