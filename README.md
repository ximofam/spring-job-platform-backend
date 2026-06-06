# Spring MVC Job Platform Backend 🚀

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_MVC-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

A robust, highly customized backend system for a Job Recruitment Platform. This project is built using **pure Spring MVC (100% Java Configuration, no XML/Spring Boot auto-magic)**, focusing on high performance, real-time communication, and AI-powered semantic search capabilities.

## ✨ Key Features

* **AI-Powered Semantic Search:** Utilizes **pgvector** in PostgreSQL to perform vector similarity searches. Text data is embedded using the highly capable **BAAI/bge-m3** model from Hugging Face, allowing for highly accurate, context-aware job and candidate matching.
* **Real-time Communication:** Features a custom-built, real-time Chat and Notification system. **Redis** is utilized not only for caching but also as a powerful **Message Broker** (Pub/Sub) to handle real-time event distribution efficiently.
* **Database-First Approach:** Database schemas and versioning are strictly managed using **Flyway** migrations. For data access, **Hibernate ORM** is used to map and query the existing database seamlessly.
* **Pure Java Configuration:** The entire Spring application context, DispatcherServlet, security, and database connections are manually configured using Java, demonstrating a deep understanding of the Spring Framework's internals.
* **Modular Architecture:** The codebase is split into distinct modules to separate concerns and maintain clean boundaries between core logic, configurations, and different API consumers.

---

## 🏗️ Project Structure

The project follows a multi-module architecture to ensure maintainability and scalability:

```text
spring-job-platform-backend/
├── api/          # Public-facing RESTful APIs for end-users (job seekers, employers)
├── admin/        # Secured APIs dedicated to platform administration and management
├── config/       # Core infrastructure setup (Spring Context, DB, Redis, Web MVC, Security)
└── core/         # Business logic layer (Entities, Repositories, Services, DTOs, Utilities)
