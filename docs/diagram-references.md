# CareerConnect — Diagram References

---

## 1. HLD Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│    ┌──────────────────┐                                             │
│    │ Student / Admin   │                                            │
│    │     Client        │                                            │
│    └────────┬─────────┘                                             │
│             │  HTTP Requests                                        │
│             ▼                                                       │
│    ┌──────────────────┐                                             │
│    │  Load Balancer    │                                            │
│    └────────┬─────────┘                                             │
│             │                                                       │
│             ▼                                                       │
│    ┌──────────────────────────────────────────────┐                 │
│    │         Spring Boot REST Application          │                 │
│    │                                                │                 │
│    │  ┌────────────────┐  ┌──────────────────┐    │                 │
│    │  │ Student/Profile │  │ Company & Drive   │    │                 │
│    │  │    Module       │  │    Module         │    │                 │
│    │  └────────────────┘  └──────────────────┘    │                 │
│    │                                                │                 │
│    │  ┌────────────────┐  ┌──────────────────┐    │                 │
│    │  │  Eligibility    │  │   Application    │    │                 │
│    │  │    Module       │  │     Module       │    │                 │
│    │  └────────────────┘  └──────────────────┘    │                 │
│    │                                                │                 │
│    │  ┌────────────────────────────────────────┐  │                 │
│    │  │       Career Advisor Module            │  │                 │
│    │  └──────────────────┬─────────────────────┘  │                 │
│    │                     │                          │                 │
│    └─────────┬───────────┼──────────────────────────┘                 │
│              │           │                                            │
│              ▼           ▼                                            │
│    ┌──────────────┐  ┌──────────────────────┐                       │
│    │  In-Memory    │  │   Advisor Service    │                       │
│    │  Storage      │  │  (localhost:11434)   │                       │
│    │ (ConcurrentH  │  │                      │                       │
│    │  ashMap)      │  │                      │                       │
│    └──────────────┘  └──────────────────────┘                       │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2. Class Diagram (LLD)

```
┌─────────────────────────┐
│       <<interface>>      │
│    EligibilityPolicy     │
│─────────────────────────│
│ + evaluate(Student,      │
│   PlacementDrive)        │
│   : EligibilityResult    │
└────────────┬────────────┘
             │ 
    ┌────────┼────────┬──────────────┬──────────────┐
    ▼        ▼        ▼              ▼              ▼
┌────────┐┌────────┐┌──────────┐┌──────────────┐┌──────────────────┐
│CgpaP.  ││BacklogP││SkillP.  ││GraduationY.P ││CompositeElig.P.  │
└────────┘└────────┘└──────────┘└──────────────┘│                  │
                                                 │ has List<Elig.P> │
                                                 └──────────────────┘

┌─────────────────────────┐
│       <<interface>>      │
│        ChatClient        │
│─────────────────────────│
│ + chat(systemPrompt,     │
│   userMessage) : String  │
└────────────┬────────────┘
             │ 
             ▼
┌─────────────────────────┐
│    OllamaChatClient      │
│─────────────────────────│
│ - baseUrl : String       │
│ - model : String         │
│ - restTemplate           │
│ + chat(...) : String     │
│ + getModel() : String    │
└─────────────────────────┘

┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   Student    │  │   Company    │  │PlacementDrive│  │ Application  │
│──────────────│  │──────────────│  │──────────────│  │──────────────│
│ - id         │  │ - id         │  │ - id         │  │ - id         │
│ - name       │  │ - name       │  │ - companyId  │  │ - studentId  │
│ - email      │  │ - sector     │  │ - role       │  │ - driveId    │
│ - programme  │  │ - description│  │ - deadline   │  │ - status     │
│ - cgpa       │  └──────────────┘  │ - minCgpa    │  │ - submittedAt│
│ - backlogs   │                    │ - maxBacklogs│  │──────────────│
│ - skills[]   │                    │ - skills[]   │  │+transitionTo │
│──────────────│                    │──────────────│  └──────┬───────┘
│+updateProfile│                    │+isDeadline   │         │ 
└──────────────┘                    │  Passed()    │         ▼
                                    └──────────────┘  ┌──────────────┐
                                                      │ApplicationSt.│
                                                      │   <<enum>>   │
                                                      │──────────────│
                                                      │ SUBMITTED    │
                                                      │ UNDER_REVIEW │
                                                      │ SHORTLISTED  │
                                                      │ SELECTED     │
                                                      │ REJECTED     │
                                                      │ WITHDRAWN    │
                                                      │──────────────│
                                                      │+canTransition│
                                                      │  To()        │
                                                      └──────────────┘

┌───────────────────┐     ┌───────────────────┐
│  <<interface>>     │     │  <<interface>>     │
│ StudentRepository  │     │ CompanyRepository  │
│───────────────────│     │───────────────────│
│ + save()           │     │ + save()           │
│ + findById()       │     │ + findById()       │
│ + findByEmail()    │     │ + existsByName()   │
│ + findAll()        │     │ + findAll()        │
└────────┬──────────┘     └────────┬──────────┘
         │                         │ 
         ▼                         ▼
┌───────────────────┐     ┌───────────────────┐
│InMemoryStudentRepo│     │InMemoryCompanyRepo │
└───────────────────┘     └───────────────────┘
```

---

## 3. Sequence Diagram: Apply to Drive

```
 Student        ApplicationController    ApplicationService    StudentRepo    DriveRepo    AppRepo    EligibilityPolicy
   │                    │                       │                  │              │           │              │
   │  POST /api/drives/ │                       │                  │              │           │              │
   │  {driveId}/apps    │                       │                  │              │           │              │
   │  {"studentId":...} │                       │                  │              │           │              │
   │───────────────────▶│                       │                  │              │           │              │
   │                    │  submitApplication()  │                  │              │           │              │
   │                    │──────────────────────▶│                  │              │           │              │
   │                    │                       │  findById(stuId) │              │           │              │
   │                    │                       │─────────────────▶│              │           │              │
   │                    │                       │  Student found   │              │           │              │
   │                    │                       │◀─────────────────│              │           │              │
   │                    │                       │                  │              │           │              │
   │                    │                       │  findById(drvId) │              │           │              │
   │                    │                       │─────────────────────────────────▶           │              │
   │                    │                       │  Drive found     │              │           │              │
   │                    │                       │◀─────────────────────────────────           │              │
   │                    │                       │                  │              │           │              │
   │                    │                       │  isDeadlinePassed()             │           │              │
   │                    │                       │  → false         │              │           │              │
   │                    │                       │                  │              │           │              │
   │                    │                       │  findByDriveIdAndStudentId()    │           │              │
   │                    │                       │──────────────────────────────────────────────▶             │
   │                    │                       │  empty           │              │           │              │
   │                    │                       │◀──────────────────────────────────────────────             │
   │                    │                       │                  │              │           │              │
   │                    │                       │  evaluate(student, drive)       │           │              │
   │                    │                       │──────────────────────────────────────────────────────────────▶
   │                    │                       │  EligibilityResult(true, [...]) │           │              │
   │                    │                       │◀──────────────────────────────────────────────────────────────
   │                    │                       │                  │              │           │              │
   │                    │                       │  save(application)              │           │              │
   │                    │                       │──────────────────────────────────────────────▶             │
   │                    │                       │◀──────────────────────────────────────────────             │
   │                    │                       │                  │              │           │              │
   │                    │  Application          │                  │              │           │              │
   │                    │◀──────────────────────│                  │              │           │              │
   │  201 Created       │                       │                  │              │           │              │
   │◀───────────────────│                       │                  │              │           │              │
```

---

## 4. Sequence Diagram: Ask Career Advisor

```
 Student        ChatController     CareerAssistantService    StudentRepo    DriveRepo    EligibilityPolicy    ChatClient    OllamaChatClient    Ollama HTTP
   │                 │                      │                    │              │              │                  │                │               │
   │  POST /api/chat │                      │                    │              │              │                  │                │               │
   │  {"studentId",  │                      │                    │              │              │                  │                │               │
   │   "driveId",    │                      │                    │              │              │                  │                │               │
   │   "message"}    │                      │                    │              │              │                  │                │               │
   │────────────────▶│                      │                    │              │              │                  │                │               │
   │                 │  handleChat()        │                    │              │              │                  │                │               │
   │                 │─────────────────────▶│                    │              │              │                  │                │               │
   │                 │                      │  findById(stuId)   │              │              │                  │                │               │
   │                 │                      │───────────────────▶│              │              │                  │                │               │
   │                 │                      │  Student profile   │              │              │                  │                │               │
   │                 │                      │◀───────────────────│              │              │                  │                │               │
   │                 │                      │                    │              │              │                  │                │               │
   │                 │                      │  findById(drvId)   │              │              │                  │                │               │
   │                 │                      │──────────────────────────────────▶│              │                  │                │               │
   │                 │                      │  Drive details     │              │              │                  │                │               │
   │                 │                      │◀──────────────────────────────────│              │                  │                │               │
   │                 │                      │                    │              │              │                  │                │               │
   │                 │                      │  evaluate(stu, drv)│              │              │                  │                │               │
   │                 │                      │─────────────────────────────────────────────────▶│                  │                │               │
   │                 │                      │  EligibilityResult │              │              │                  │                │               │
   │                 │                      │◀─────────────────────────────────────────────────│                  │                │               │
   │                 │                      │                    │              │              │                  │                │               │
   │                 │                      │  chat(sysPrompt, userMsg)         │              │                  │                │               │
   │                 │                      │──────────────────────────────────────────────────────────────────────▶               │               │
   │                 │                      │                    │              │              │                  │  POST /api/chat│               │
   │                 │                      │                    │              │              │                  │  (Ollama JSON) │               │
   │                 │                      │                    │              │              │                  │───────────────▶│               │
   │                 │                      │                    │              │              │                  │                │  HTTP POST    │
   │                 │                      │                    │              │              │                  │                │──────────────▶│
   │                 │                      │                    │              │              │                  │                │  LLM response │
   │                 │                      │                    │              │              │                  │                │◀──────────────│
   │                 │                      │                    │              │              │                  │  parsed answer │               │
   │                 │                      │                    │              │              │                  │◀───────────────│               │
   │                 │                      │  answer string     │              │              │                  │                │               │
   │                 │                      │◀──────────────────────────────────────────────────────────────────────               │               │
   │                 │  ChatResponse        │                    │              │              │                  │                │               │
   │                 │  {answer, model,     │                    │              │              │                  │                │               │
   │                 │   advisory: true}    │                    │              │              │                  │                │               │
   │                 │◀─────────────────────│                    │              │              │                  │                │               │
   │  200 OK         │                      │                    │              │              │                  │                │               │
   │◀────────────────│                      │                    │              │              │                  │                │               │
```
