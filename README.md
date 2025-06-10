# Payment Tracker Service

A simple Spring Boot application that tracks payments in different currencies and logs payment summaries at regular intervals.
(Assuming payments cannot be zero, it allow positive or negative amounts)
## Features

- Record payments in different currencies
- Automatic payment summary logging
- Thread-safe payment tracking using ConcurrentHashMap
- Configurable logging interval

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 2.7.18

## Getting Started

1. Clone the repository using this URL: https://github.com/kaushiksp159/spring-boot-payment-tracker.git
2. Navigate to the project directory:
   cd spring-boot-payment-tracker
3. Build the project:
    mvn clean install
4. Run the application: 
    mvn spring-boot:run



## Configuration 

- The application can be configured through src/main/resources/application.properties:
   server.port: Server port (default: 8080)

## API Endpoints

- Record Payment : POST /payment/{currency}/{amount} - This endpoint keeps a record of payments. Each payment includes a
  currency and an amount.

  currency: Currency code (e.g., USD, EUR)
  amount: Payment amount (integer)
 For example: curl -X POST http://localhost:8080/payment/USD/100 (in CMD)


