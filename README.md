# Online Store Microservice

## Introduction

This is an online store microservice that allows customers to order products. The service is built using a microservice architecture and utilizes various technologies and tools to provide a robust and scalable solution.

## Features

- The service uses Kafka for messaging when an order is placed and sends notifications to customers in real-time.
- The service uses Eureka for service discovery and API Gateway for routing different services, and it is secure by Keycloak.
- The service uses resilience4j for circuit breaker and retries, Micrometer and Zipkin for distributed tracing and Prometheus and Grafana for metrics collection and visualization.
- All services are dockerized and deployed using Jib and Docker Compose

To understand it better, I provided some information about each section.

## Microservices

There are multiple microservices provided:

- api-gateway
- discovery-server
- inventory-service
- notification-service
- order-service
- product-service

## Setup

To run the service locally, you'll need to have the following installed on your machine:

- Java 17
- Docker
- Docker Compose

1. Clone the repository: `git clone https://github.com/hamidfarmani/online-store.git`
2. Build and start the services using Docker Compose: `docker-compose up -d`
3. The service should now be running at `http://localhost:8080`

## Deployment

The service is designed to be easily deployed in a containerized environment. To deploy the service, you can use the Docker images built by Jib and the Docker Compose file provided.

## Monitoring and troubleshooting

Prometheus and Grafana have been added for metrics collection and visualization and Zipkin for distributed tracing. These tools provide detailed insights into the performance and behavior of the microservices and the interactions between them.

## Explanations

The following mentioned tools were selected to provide robust and scalable solution and to learn and test these tools.

### Security

The service is secured using KeyCloak, an open-source identity and access management solution. KeyCloak provides authentication and authorization for the microservices and allows for easy integration with other identity providers.

You can access KeyCloak on`http://localhost:8080/`

### Service Discovery

The service uses Eureka, a service registry for resilient mid-tier load balancing and failover. Eureka allows for easy service discovery, registration and de-registration of services, providing a stable and self-healing service discovery mechanism.

You can access Eureka on`http://localhost:8761/`

### Messaging

The service uses Kafka, a distributed streaming platform, for messaging. Kafka allows for real-time messaging and notifications to customers when an order is placed, providing a reliable and scalable solution for messaging.

### Circuit Breaker and Retries

The service uses Resilience4J, a fault tolerance library, for circuit breaker and retries. Resilience4J helps to prevent cascading failures and improve the overall resilience of the system by providing a circuit breaker pattern and retry mechanism.

### Distributed Tracing

The service uses Micrometer and Zipkin for distributed tracing. This allows for the tracing of requests across multiple microservices, making it easy to identify and troubleshoot issues within the system.

### Metrics Collection

The service uses Prometheus for metrics collection. Prometheus is a powerful and flexible monitoring solution that allows for easy collection and querying of metrics from the services.

You can access Prometheus on`http://localhost:9090/`

### Service Visualization

The service uses Grafana for service visualization. Grafana provides an easy-to-use interface for visualizing and analyzing metrics collected by Prometheus, making it easy to understand the performance and health of the system.

You can access Grafana on`http://localhost:3000/` and as described in `docker-compose.yml` file, its username is `admin` and the password is `password`.
