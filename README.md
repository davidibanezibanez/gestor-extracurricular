# Extracurricular Activity Manager

## Overview

**Extracurricular Activity Manager** is a scalable, distributed platform designed to manage the entire lifecycle of university extracurricular activities. Unlike monolithic applications, this project is built on a **Microservices Architecture**, ensuring modularity, scalability, and independent deployment of services.

The system allows students to browse and enroll in activities, while administrators can manage users, approve events, and generate detailed reports through a reactive frontend built with **Vue 3 and TypeScript**.

## System Architecture

The ecosystem consists of 4 specialized backend services and a modern frontend gateway:

| Service | Port | Description |

| **Auth Service** | `8081` | Handles JWT authentication, registration, and role management (Admin/Student). |

| **User Service** | `8082` | Manages detailed user profiles and academic information. |

| **Activity Service** | `8083` | Core logic for creating activities, managing schedules, and handling enrollments. |

| **Report Service** | `8084` | Aggregates data to generate insights and integrates with external APIs for data enrichment. |

| **Frontend App** | `5173` | A unified UI built with Vue 3, TypeScript, and Pinia. |

## Tech Stack

### Backend (Microservices)
* **Framework:** Spring Boot 3 (Java 17)
* **Security:** Spring Security + JWT (Stateless authentication across services)
* **Database:** MySQL (Each service manages its own schema/tables for loose coupling)
* **Communication:** RESTful APIs using `RestTemplate` / `WebClient`
* **Testing:** JUnit 5, Mockito

### Frontend
* **Framework:** Vue 3 (Composition API)
* **Language:** TypeScript
* **State Management:** Pinia
* **Styling:** Custom CSS / Tailwind components
* **Build Tool:** Vite

### DevOps & Tools
* **Containerization:** Docker & Docker Compose
* **API Testing:** Postman Collection (included in `/postman-gestor-extracurricular`)
* **CI/CD:** GitLab CI configurations included (`.gitlab-ci.yml`)

## Getting Started

The easiest way to run the entire system is using Docker Compose.

### Prerequisites
* Docker Desktop installed
* Git

### Deployment Steps

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/davidibanezibanez/gestor-extracurricular.git](https://github.com/davidibanezibanez/gestor-extracurricular.git)
    cd gestor-extracurricular
    ```

2.  **Launch the Ecosystem**
    Navigate to the deployment folder and start the services:
    ```bash
    cd deploy-gestor-extracurricular
    docker-compose up -d --build
    ```

3.  **Verify Status**
    Wait for a few minutes for all Java services to initialize. You can check the logs:
    ```bash
    docker-compose logs -f
    ```

4.  **Access the Application**
    * **Frontend UI:** `http://localhost:5173`
    * **API Gateways:** Directly accessible via their respective ports (e.g., Auth at `8081`).

## Testing with Postman

A full Postman collection is provided to test the microservices independently.
1.  Import `postman-gestor-extracurricular/postman-gestor-extracurricular.json` into Postman.
2.  Start by creating a user via the **Auth Service** endpoints.
3.  Use the returned **Bearer Token** to authenticate requests to the Activity and User services.

## Project Structure

```text
/
├── backend-gestor-extracurricular/
│   ├── backend-activity-service-gestor-extracurricular/
│   ├── backend-auth-service-gestor-extracurricular/
│   ├── backend-report-service-gestor-extracurricular/
│   └── backend-user-service-gestor-extracurricular/
├── frontend-gestor-extracurricular/  # Vue 3 + TypeScript Client
├── deploy-gestor-extracurricular/    # Docker orchestration
└── postman-gestor-extracurricular/   # API Documentation

```

## Contributing

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/NewMicroservice`).
3. Commit your changes.
4. Push to the branch.
5. Open a Pull Request.

## Contact

David Ibáñez - https://www.linkedin.com/in/davidibanezibanez/

Project Link: [https://github.com/davidibanezibanez/gestor-extracurricular](https://www.google.com/search?q=https://github.com/davidibanezibanez/gestor-extracurricular)
