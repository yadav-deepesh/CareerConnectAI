# CareerConnect - REST API Contract

## Base URL
`http://localhost:8080`

---

## 1. Student Management

### 1.1 Create Student Profile
- **Method**: `POST`
- **Path**: `/api/students`
- **Request Body**:
```json
{
  "name": "Rahul Verma",
  "email": "rahul.verma@example.com",
  "programme": "B.Tech Computer Science",
  "graduationYear": 2026,
  "cgpa": 8.5,
  "activeBacklogs": 0,
  "skills": ["Java", "Spring Boot", "SQL"]
}
```
- **Responses**:
  - `201 Created`: Student created successfully. Returns created `StudentResponse`.
  - `400 Bad Request`: Field validation failed (e.g. invalid email format or negative CGPA).
  - `409 Conflict`: Email already exists.

### 1.2 Get Student Profile
- **Method**: `GET`
- **Path**: `/api/students/{studentId}`
- **Responses**:
  - `200 OK`: Returns `StudentResponse`.
  - `404 Not Found`: Student ID does not exist.

### 1.3 Update Student Profile
- **Method**: `PUT`
- **Path**: `/api/students/{studentId}`
- **Request Body**:
```json
{
  "name": "Rahul Verma",
  "programme": "B.Tech Computer Science",
  "graduationYear": 2026,
  "cgpa": 8.8,
  "activeBacklogs": 0,
  "skills": ["Java", "Spring Boot", "SQL", "Docker"]
}
```
- **Responses**:
  - `200 OK`: Student updated successfully.
  - `404 Not Found`: Student ID does not exist.

---

## 2. Company Management

### 2.1 Create Company
- **Method**: `POST`
- **Path**: `/api/companies`
- **Request Body**:
```json
{
  "name": "Acme Technologies",
  "sector": "Software & IT",
  "description": "Enterprise cloud solutions provider"
}
```
- **Responses**:
  - `201 Created`: Returns `CompanyResponse`.
  - `409 Conflict`: Company name already exists.

### 2.2 Get Company Details
- **Method**: `GET`
- **Path**: `/api/companies/{companyId}`
- **Responses**:
  - `200 OK`: Returns `CompanyResponse`.
  - `404 Not Found`: Company ID does not exist.

---

## 3. Placement Drive Management

### 3.1 Create Placement Drive
- **Method**: `POST`
- **Path**: `/api/drives`
- **Request Body**:
```json
{
  "companyId": "CMP-001",
  "role": "Backend Engineer",
  "location": "Bengaluru",
  "packageLpa": 12.5,
  "deadline": "2026-12-31",
  "requiredSkills": ["Java", "Spring Boot"],
  "minCgpa": 7.5,
  "maxBacklogs": 0,
  "allowedProgrammes": ["B.Tech Computer Science", "B.Tech Information Technology"],
  "graduationYear": 2026
}
```
- **Responses**:
  - `201 Created`: Drive created.
  - `404 Not Found`: Referenced `companyId` does not exist.
  - `400 Bad Request`: Deadline in the past or validation error.

### 3.2 List / Search Placement Drives
- **Method**: `GET`
- **Path**: `/api/drives`
- **Query Params** (Optional): `companyId`, `role`, `status`, `location`
- **Responses**:
  - `200 OK`: List of matching `DriveResponse` objects.

### 3.3 Check Student Eligibility for Drive
- **Method**: `GET`
- **Path**: `/api/drives/{driveId}/eligibility/{studentId}`
- **Responses**:
  - `200 OK`:
```json
{
  "studentId": "STU-001",
  "driveId": "DRV-100",
  "eligible": false,
  "reasons": [
    "Minimum CGPA required: 8.0; current CGPA: 7.2",
    "Missing required skill(s): Spring Boot"
  ]
}
```
  - `404 Not Found`: Drive or Student not found.

---

## 4. Application Lifecycle Management

### 4.1 Submit Application
- **Method**: `POST`
- **Path**: `/api/drives/{driveId}/applications`
- **Request Body**:
```json
{
  "studentId": "STU-001"
}
```
- **Responses**:
  - `201 Created`: Application created (`status`: `SUBMITTED`).
  - `400 Bad Request`: Student is ineligible or drive deadline has passed.
  - `404 Not Found`: Drive or Student ID invalid.
  - `409 Conflict`: Duplicate application (student already applied).

### 4.2 List Student Applications
- **Method**: `GET`
- **Path**: `/api/students/{studentId}/applications`
- **Responses**:
  - `200 OK`: List of applications submitted by student.

### 4.3 Update Application Status (Coordinator Action)
- **Method**: `PATCH`
- **Path**: `/api/applications/{applicationId}/status`
- **Request Body**:
```json
{
  "status": "SHORTLISTED"
}
```
- **Responses**:
  - `200 OK`: Status updated.
  - `409 Conflict`: Invalid state transition (e.g. `SELECTED` -> `SUBMITTED`).

---

## 5. Career Advisor Integration

### 5.1 Ask Career Advisor
- **Method**: `POST`
- **Path**: `/api/chat`
- **Request Body**:
```json
{
  "studentId": "STU-001",
  "driveId": "DRV-100",
  "message": "Why am I ineligible for this drive and what should I improve?"
}
```
- **Responses**:
  - `200 OK`:
```json
{
  "answer": "Based on your current profile, you do not meet the minimum CGPA requirement of 8.0 (your CGPA is 7.2) and you are missing Spring Boot from your skills list. I recommend working on Spring Boot projects to improve your skill set.",
  "model": "llama3.2",
  "advisory": true
}
```
  - `503 Service Unavailable`: Advisor service down or unreachable. Core placement APIs continue functioning.
