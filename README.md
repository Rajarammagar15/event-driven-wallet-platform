# 💳 Event-Driven Wallet Platform

A production-grade distributed wallet platform built with **Java 21**, **Spring Boot**, **Apache Kafka**, **Redis**, **PostgreSQL**, and **MongoDB** — designed around event-driven microservices architecture with a Kafka-native communication layer.

> Built as a personal project to deeply understand Kafka, distributed systems, and microservices design in the context of financial platforms.

---

## 🏗️ Architecture Overview

```
Client
  └── API Gateway (Auth · Rate Limiting · Load Balancing)
        └── Wallet Service (Core)
              ├── PostgreSQL  (transactional store)
              ├── Redis       (balance cache — write-through)
              └── Kafka       (event publisher)
                    ├── wallet.credit
                    ├── wallet.debit
                    ├── fraud.alert
                    ├── wallet.dlq    (dead letter queue)
                    └── wallet.retry
                          ├── Notification Service
                          ├── Audit Service
                          ├── Fraud Detection Service
                          ├── Reversal Service        [in progress]
                          └── Reporting Service       [in progress]
```

> Full HLD and service LLD diagrams available in the [`/docs`](/docs) folder.

---

## ✅ Current Status

| Service | Status | Storage |
|---|---|---|
| Wallet Service | ✅ Complete | PostgreSQL + Redis |
| Notification Service | ✅ Complete | PostgreSQL |
| Audit Service | ✅ Complete | MongoDB |
| Fraud Detection Service | ✅ Complete | Redis + MongoDB |
| Reversal Service | 🔧 In Progress | PostgreSQL (Wallet DB) |
| Reporting Service | 🔧 In Progress | MongoDB |

---

## ⚙️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Messaging | Apache Kafka + Zookeeper |
| Cache | Redis |
| Relational DB | PostgreSQL |
| Document DB | MongoDB |
| Containerisation | Docker + Docker Compose |

---

## 🔍 Services Deep Dive

### 💰 Wallet Service
The core service and single entry point for all wallet operations.

**APIs:**
- `POST /create` — Create a new wallet
- `POST /{walletId}/credit` — Credit wallet
- `POST /{walletId}/debit` — Debit wallet
- `GET /{walletId}/balance` — Get balance

**Key Design Decisions:**
- **Pessimistic locking** (`PESSIMISTIC_WRITE`) on debit/credit operations — prevents race conditions under concurrent load
- **Redis write-through cache** for balance reads — avoids hitting PostgreSQL on every `GET /balance`
- **Kafka event publishing** after every transaction — downstream services are fully decoupled; Wallet Service publishes and moves on
- **Sequence:** `Controller → Service → Repository → PostgreSQL commit → Redis cache update → Kafka publish`

**Kafka Topics Published:**
```
wallet.create   — new wallet created
wallet.credit   — credit transaction event
wallet.debit    — debit transaction event
wallet.dlq      — dead letter queue for failed consumer processing
wallet.retry    — retry queue before DLQ fallback
```

---

### 🔔 Notification Service
Consumes wallet transaction events and handles notification processing.

- Kafka Consumer Group: `notification-group`
- Topic: `wallet-transactions`
- Persists `NotificationEventLog` to **PostgreSQL**
- Also consumes `FraudAlertEvent` from Fraud Service — sends fraud notifications and persists log

---

### 📋 Audit Service
Immutable audit trail for every wallet transaction.

- Kafka Consumer Group: `audit-log`
- Topic: `wallet-transaction`
- Maps events to `AuditTransactionLog` entity
- Persists to **MongoDB** (flexible schema — suitable for arbitrary event payloads)

**Stored fields:** `id`, `transactionId`, `walletId`, `customerId`, `eventType`, `payload`, `receivedAt`

---

### 🛡️ Fraud Detection Service
Dual-flow fraud detection — synchronous pre-transaction blocking and asynchronous post-transaction monitoring.

**Pre-Transaction Flow (Sync):**
```
/fraud/check → FraudController → FraudService → Redis
```
Redis tracks velocity counters, daily limits, and max amounts in real-time. Returns `allowed/blocked` before the transaction commits.

**Post-Transaction Flow (Async):**
```
Kafka (wallet-transaction / fraud-group) → FraudConsumer
  → RuleCheckEngine (checkMaxAmount · checkDailyLimit · checkVelocity)
  → FraudRepository (MongoDB)
  → FraudProducer publishes FraudAlertEvent (fraud.alert topic)
  → Notification Service consumes alert
```

**Why dual-flow?**
Synchronous path handles blocking decisions under strict latency constraints. Asynchronous path handles audit trail and pattern analysis where latency doesn't matter. Redis for speed. MongoDB for history.

**Stored fields:** `id`, `transactionAmount`, `createdAt`, `type`, `customerId`, `walletId`, `transactionId`, `triggeredRule`, `fraudStatus`

---

## 🏛️ Key Design Decisions

**1. Why Kafka between services (not REST)?**
Wallet Service needs to trigger notifications, fraud checks, audit logging, and reporting for every transaction. REST would mean 4 synchronous calls — any single failure breaks the transaction. With Kafka, Wallet Service publishes once and moves on. Each consumer handles its own failures independently.

**2. Why database-per-service?**
No shared databases. Each service owns its data store — enabling independent scaling, deployment, and failure isolation. Shared databases couple services at the data layer even when APIs are separate.

**3. Why Redis at two levels?**
Wallet Service uses Redis as a write-through balance cache. Fraud Service uses a separate Redis instance for atomic velocity counters (`INCR`) under concurrent load. Same technology, two different jobs, two independent instances.

**4. Why Pessimistic Locking on debit/credit?**
Concurrent debit and credit on the same wallet without locking risks double-spend or incorrect balances. `PESSIMISTIC_WRITE` acquires a DB-level lock for the duration of the transaction. Future scale path: Redis distributed lock.

**5. DLQ and retry topics designed in from the start**
`wallet.dlq` and `wallet.retry` are not afterthoughts — they're part of the initial Kafka topic design, ensuring no event is silently lost if a consumer fails.

---

## 📊 Observability

| Tool | Purpose |
|---|---|
| ELK Stack | Centralised log aggregation and search |
| Spring Actuator / Metrics | Health checks, JVM metrics, endpoint monitoring |
| Postman / JMeter | API testing and load testing |

---

## 🚀 CI/CD Pipeline

```
Git → Jenkins / GitHub Actions → Docker → AWS EC2 / OpenShift
```

---

## 🐳 Running Locally

**Prerequisites:** Docker, Docker Compose, Java 21

```bash
# Clone the repository
git clone https://github.com/Rajarammagar15/event-driven-wallet-platform
cd event-driven-wallet-platform

# Start infrastructure (Kafka, Zookeeper, PostgreSQL, Redis, MongoDB)
docker-compose up -d

# Run Wallet Service
cd wallet-service
./mvnw spring-boot:run

# Run other services in separate terminals
cd notification-service && ./mvnw spring-boot:run
cd audit-service && ./mvnw spring-boot:run
cd fraud-service && ./mvnw spring-boot:run
```

---

## 📁 Project Structure

```
wallet-platform/
├── wallet-service/
├── common-events/
├── notification-service/
├── audit-service/
├── fraud-service/
├── reversal-service/        [in progress]
├── reporting-service/       [in progress]
├── docker-compose.yml
└── docs/
    ├── HLD.png
    ├── wallet-service-lld.png
    ├── fraud-service-lld.png
    ├── audit-service-lld.png
    └── notification-service-lld.png
```

---

## 🗺️ Roadmap

- [ ] Reversal Service — consume `wallet.retry` events, write reversal back to Wallet PostgreSQL
- [ ] Reporting Service — aggregate transaction data via MongoDB for analytics queries
- [ ] API Gateway — implement as standalone Spring Cloud Gateway service
- [ ] Redis Distributed Lock — future scale path for high-concurrency debit/credit
- [ ] RAG pipeline — LLM over internal docs for architectural decision support

---

## 📬 Connect

Built by **Rajaram Magar** — Backend Engineer focused on fintech, distributed systems, and event-driven architecture.

[LinkedIn](https://www.linkedin.com/in/rajaram-magar/) · [GitHub](https://github.com/Rajarammagar15)