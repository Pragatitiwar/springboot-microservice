# Spring Boot Microservices

A hands-on backend microservices project built with **Java, Spring Boot, Kafka, Docker, Kubernetes, and Keycloak**.

The project is being developed incrementally to practice and demonstrate modern backend engineering concepts, including **REST APIs, database integration, synchronous and event-driven communication, API Gateway, authentication and authorization, containerization, Kubernetes deployment, monitoring, and alerting**.

## Architecture

```text
                              Client
                                │
                                │ JWT
                                ▼
                         ┌──────────────┐
                         │   Keycloak   │
                         │ Authentication│
                         └──────┬───────┘
                                │
                                ▼
                         ┌──────────────┐
                         │ API Gateway  │
                         │    :8080     │
                         └──────┬───────┘
                                │
                    ┌───────────┴───────────┐
                    │                       │
                    ▼                       ▼
             Product Service          Order Service
                  :8081                    :8082
                    │                       │
                    ▼                       │
                  MySQL                     │
                                            │
                              ┌─────────────┘
                              │
                              │ REST
                              ▼
                       Product Service

                         Order Service
                              │
                              │ Kafka Events
                              ▼
                            Kafka

                         Kubernetes
                              │
                    ┌─────────┴─────────┐
                    │                   │
               Prometheus           Grafana
                    │                   │
                    └─────────┬─────────┘
                              │
                           Alerting
```

## Components

### API Gateway

Spring Cloud Gateway is used as the single entry point for external API requests.

* Centralized request routing
* Product Service routing
* Order Service routing
* JWT authentication
* Role-based authorization
* WebFlux-based reactive gateway
* Acts as an OAuth2 Resource Server

Example routing:

```text
Client
  │
  ▼
API Gateway :8080
  │
  ├── /products/** → Product Service :8081
  │
  └── /orders/**   → Order Service :8082
```

### Authentication & Authorization

The project uses **Keycloak** as the Identity Provider.

Keycloak is responsible for:

* User authentication
* JWT access-token issuance
* Role management

The API Gateway validates JWT access tokens before forwarding requests.

Current roles:

```text
USER
ADMIN
```

Keycloak roles are mapped to Spring Security authorities.

Example:

```text
Keycloak
   │
   │ realm_access.roles
   ▼
["USER"]
   │
   ▼
ROLE_USER
```

Role-based authorization is implemented at the Gateway.

For example:

```text
GET /products/**
    USER  → Allowed
    ADMIN → Allowed

DELETE /products/**
    USER  → 403 Forbidden
    ADMIN → Allowed
```

Authentication and authorization behavior:

```text
No / invalid JWT
       │
       ▼
401 Unauthorized

Valid JWT
    │
    ▼
Insufficient role
    │
    ▼
403 Forbidden

Valid JWT + required role
    │
    ▼
Request forwarded
```

### Product Service

* Spring Boot REST API
* Product CRUD operations
* MySQL database integration
* Input validation
* Exception handling
* Spring Boot Actuator
* Prometheus metrics

### Order Service

* Spring Boot REST API
* Order creation and management
* Synchronous communication with Product Service
* Kafka-based event communication
* MySQL database integration
* Spring Boot Actuator
* Prometheus metrics

### Service-to-Service Communication

The project demonstrates both synchronous and asynchronous communication.

#### Synchronous communication

Order Service communicates with Product Service using REST when an immediate response is required.

```text
Order Service
      │
      │ REST
      ▼
Product Service
```

#### Asynchronous communication

Kafka is used for event-driven communication where services do not need to wait for an immediate response.

```text
Order Service
      │
      │ Event
      ▼
    Kafka
      │
      ▼
   Consumers
```

### Kafka

Kafka is used for **event-driven communication** between microservices.

The project demonstrates concepts such as:

* Producers
* Consumers
* Topics
* Consumer groups
* Partitions
* Offsets
* Asynchronous event processing

### Docker

The services and supporting infrastructure can be containerized using Docker and Docker Compose.

Docker is primarily used for local development and running the supporting infrastructure.

### Kubernetes

The application is deployed to Kubernetes using:

* Deployments
* Services
* NodePort services
* Kubernetes configuration files
* Kubernetes-based service discovery

Kubernetes provides the orchestration layer for running the containerized microservices.

### Monitoring

The project uses:

* **Prometheus** for metrics collection
* **Grafana** for visualization
* **Spring Boot Actuator + Micrometer** for application metrics
* **ServiceMonitor** resources for Kubernetes-based Prometheus scraping

The Grafana dashboard currently monitors:

* HTTP request rate
* HTTP request count
* HTTP error rate
* Average response time
* JVM heap memory
* JVM heap usage
* CPU usage

### Alerting

Grafana alerting is configured to detect a high HTTP error rate for the Product Service.

Current example:

```text
Product Service HTTP error rate > 5%
                │
                ↓
          Pending for 2 minutes
                │
                ↓
             Firing
```

The alert has been tested successfully using an intentional HTTP error and verified through the **Normal → Pending → Firing → Normal** lifecycle.

## Technology Stack

| Technology           | Purpose                          |
| -------------------- | -------------------------------- |
| Java                 | Backend development              |
| Spring Boot          | Microservices framework          |
| Spring Data JPA      | Database access                  |
| Spring Cloud Gateway | API Gateway and request routing  |
| Spring Security      | Authentication and authorization |
| Keycloak             | Identity and access management   |
| MySQL                | Relational database              |
| Kafka                | Event-driven communication       |
| Docker               | Containerization                 |
| Kubernetes           | Container orchestration          |
| Prometheus           | Metrics collection               |
| Grafana              | Monitoring and visualization     |
| Maven                | Build and dependency management  |

## Project Structure

```text
springboot-microservices/
│
├── product-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── api-gateway/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── kubernetes/
│   ├── product-service/
│   ├── order-service/
│   ├── kafka/
│   ├── mysql/
│   └── monitoring/
│
├── docker-compose.yml
├── README.md
└── .gitignore
```

> The exact directory structure may evolve as the project continues to grow.

## Monitoring Access

When running locally, Grafana can be accessed through the Kubernetes port-forward:

```bash
kubectl port-forward svc/monitoring-grafana -n monitoring 3000:80
```

Grafana:

```text
http://localhost:3000
```

Prometheus can be accessed using:

```bash
kubectl port-forward svc/monitoring-kube-prometheus-prometheus -n monitoring 9090:9090
```

Prometheus:

```text
http://localhost:9090
```

## Kubernetes Monitoring

The application services are monitored using Kubernetes `ServiceMonitor` resources.

The monitoring flow is:

```text
Spring Boot Application
        │
        ↓
Actuator / Micrometer
        │
        ↓
ServiceMonitor
        │
        ↓
Prometheus
        │
        ↓
Grafana
        │
        ↓
Alerts
```

## Security Flow

The authentication and authorization flow is:

```text
Client
   │
   │ Login
   ▼
Keycloak
   │
   │ JWT Access Token
   ▼
Client
   │
   │ Authorization: Bearer <JWT>
   ▼
API Gateway
   │
   ├── Validate JWT
   │
   ├── Extract roles
   │
   ├── Authenticate request
   │
   └── Authorize request
   │
   ▼
Microservices
```

## Project Goals

This project is being built incrementally to gain practical experience with:

* Microservice development
* REST API design
* Synchronous service-to-service communication
* Event-driven architecture
* Kafka
* API Gateway
* Authentication and authorization
* JWT
* Keycloak
* Docker
* Kubernetes
* Kubernetes service discovery
* Application monitoring
* Prometheus
* Grafana
* Alerting
* CI/CD

More components and improvements will be added as the project evolves.
