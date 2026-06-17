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

## Architecture

(HLD Image in docs folder)

## Wallet Service LLD

(LLD Image in docs folder)

## Tech Stack

- Java 21
- Spring Boot 3
- PostgreSQL
- Redis
- Apache Kafka
- Docker

## Current Services

### Wallet Service
- Create Wallet
- Credit Wallet
- Debit Wallet
- Get Balance
- Redis Cache
- PostgreSQL
- Kafka Producer

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

## Infrastructure

- Kafka
- Zookeeper
- PostgreSQL
- Redis
- Docker Compose
- MongoDB

## Future Services

- Fraud Service
- Reporting Service