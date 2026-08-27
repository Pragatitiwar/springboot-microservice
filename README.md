# Spring Boot Microservices

A hands-on backend microservices project built with **Java, Spring Boot, Kafka, Docker, and Kubernetes**.

The project is being developed incrementally to practice and demonstrate modern backend engineering concepts, including **REST APIs, database integration, event-driven communication, containerization, Kubernetes deployment, monitoring, and alerting**.

## Architecture

```text
                         Spring Boot Microservices
                                  │
                 ┌────────────────┴────────────────┐
                 │                                 │
          Product Service                    Order Service
                 │                                 │
              MySQL                             Kafka
                 │                                 │
                 └────────────────┬────────────────┘
                                  │
                             Kubernetes
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
               Prometheus                   Grafana
                    │                           │
                    └─────────────┬─────────────┘
                                  │
                              Alerting
```

## Components

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
* Kafka-based event communication
* MySQL database integration
* Spring Boot Actuator
* Prometheus metrics

### Kafka

Kafka is used for **event-driven communication** between microservices.

### Docker

The services and supporting infrastructure can be containerized using Docker and Docker Compose.

### Kubernetes

The application is deployed to Kubernetes using:

* Deployments
* Services
* NodePort services
* Kubernetes configuration files

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

| Technology      | Purpose                         |
| --------------- | ------------------------------- |
| Java            | Backend development             |
| Spring Boot     | Microservices framework         |
| Spring Data JPA | Database access                 |
| MySQL           | Relational database             |
| Kafka           | Event-driven communication      |
| Docker          | Containerization                |
| Kubernetes      | Container orchestration         |
| Prometheus      | Metrics collection              |
| Grafana         | Monitoring and visualization    |
| Maven           | Build and dependency management |

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

## Project Goals

This project is being built incrementally to gain practical experience with:

* Microservice development
* REST API design
* Database integration
* Event-driven architecture
* Kafka
* Docker
* Kubernetes
* Application monitoring
* Prometheus
* Grafana
* Alerting
* CI/CD

More components and improvements will be added as the project evolves.
