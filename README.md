# Event Driven Wallet Platform

A distributed wallet platform built using Spring Boot, Kafka, Redis, PostgreSQL, and MongoDB.

## Current Features

- Create Wallet
- Credit Wallet
- Debit Wallet
- Get Balance
- Redis Cache
- Kafka Event Publishing
- Pessimistic Locking
- Notification Consumer
- Audit Consumer
- Fraud Service and Fraud Alerts 

## Architecture

(HLD Image in docs folder)

## Wallet Service LLD

(LLD Image in docs folder)

## Tech Stack

- Java 21
- Spring Boot 4
- PostgreSQL
- Redis
- Apache Kafka
- Docker
- MongoDB

## Current Services

### Wallet Service
- Create Wallet
- Credit Wallet
- Debit Wallet
- Get Balance
- Redis Cache
- PostgreSQL
- Wallet Transaction published to Kafka Topic

### Notification Service
- Kafka Consumer
- Consumer Group
- Notification Processing
- PostgreSQL Persistence

### Audit Service
- Kafka Consumer
- Consumer Group
- Audit Log Processing
- MongoDB Persistence

### Fraud Service
- Kafka Consumer
- Consumer Group
- Fraud Log Processing
- MongoDB Persistence
- Fraud Alert published to Kafka Topic
- Notification Service to Consume, Send and Persist log to Postgre


## Infrastructure
- Kafka
- Zookeeper
- PostgreSQL
- Redis
- Docker Compose
- MongoDB

## Future Services
- Reporting Service