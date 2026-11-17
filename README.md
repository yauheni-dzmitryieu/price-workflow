# Price-workflow

## About The Project
High-Performance Caching Layer for a Pricing API
**Goal**

Build a Spring Boot service exposing GET /price/{productId} backed by a relational DB, with two
cache layers (in-process + Redis). The system must meet latency, throughput, consistency, and
observability requirements without prescribing any specific caching strategies or design
patterns.

**Functional Scope**
Domain
* Product: id, sku (unique), name, basePrice
* PriceAdjustment: id, productId, type ∈ {PROMO, TAX, FEE}, value, mode ∈ {ABSOLUTE,
* PERCENT}, updatedAt
* Final price: basePrice + sum(ABSOLUTE) + sum(PERCENT * basePrice)

**Endpoints**
GET /price/{productId}
Returns { productId, basePrice, adjustments[], finalPrice }

POST /admin/price/{productId}/adjustments
Non-Functional Requirements

**Caching**
Two layers: an in-process cache and a Redis cache.
Configurable limits and expirations on both layers.
Under concurrent access, the system must avoid unnecessary duplicate DB loads and
avoid thundering spikes when cached entries expire.
When data changes, all application replicas must begin serving the updated value quickly
(see SLA below). No stale data should be served beyond the allowed window.
The solution must operate correctly if Redis is down

**Consistency & Freshness SLA**
After a successful write via admin API, all replicas must serve the updated price within ≤ 1
second on average and ≤ 3 seconds at worst during tests.
No user request should receive an older value beyond the worst-case window during normal
operation.

**Observability**
Health endpoints for app
Structured logs with request correlation IDs.
Optional - Metrics for: request latency percentiles (p50/p95/p99), cache hit/miss per layer,
load time from DB, number of suppressed duplicate loads, number of refreshed entries, error
rates.

**Performance Targets (guidance; report actuals)**
**Before cache (DB only)**: record baseline latencies/throughput.
With cache:
  p95 for frequently requested items < 10 ms
  p99 < 20 ms
If your hardware/env differs, provide measurements and explain variance.

**Data & Seed**
* SQL migrations.
* Seed at least 100k products; 5–10% have 1–3 adjustments.

**Configuration**
* Externalize: DB pool, in-process cache sizing/expirations, Redis host/db/prefix/expirations,
feature toggles to enable/disable each cache layer, and any background refresh windows.
* Provide sane defaults in application.yml.
Fault Handling (outcome-based)

* If Redis is unavailable: service remains functional; metrics make this obvious; latency may
increase but remains stable; no data corruption.
* Provide timeouts and clear error responses for genuine failures.
Deliverables
1. Code:
   api (controllers/dto)
   domain (services/models)
   bench (load scripts/config)
2. Docker Compose: Postgres, Redis, Grafana (or Similar - optionall), App (≥2 replicas to
   demonstrate cross-replica freshness).
3. README.md:
   High-level architecture diagram
   Data model overview
   How to run locally (profiles)
   Benchmark methodology, raw results, analysis vs targets
   Known limitations and trade-offs (describe outcomes, not techniques)
4. DB migrations and seed tool
5. Postman collection or HTTP scripts
6. Metrics screenshots (Actuator/Prometheus/Grafana optional)
   Acceptance Criteria
   Correct final price calculation with tests (unit + integration)
   Two cache layers function as configured;
   After writes, all replicas serve new values within the specified SLA windows
   Under expiry stress, DB load remains controlled; latency remains stable
   Codebase is clean, configurable, and well-documented

## 📋 Table of Contents

- [Getting Started](documents/GETTING_STARTED.md) - Instruction project up and running on your local machine for
  development and testing purposes.
- [Project Architecture](documents/PROJECT_ACRHITECTURE.md) - Height level project architecture
- [Metrics](documents/METRICS.md)
- [Performance Test result](documents/PERFOMANCE_TEST_RESULT.md)
- [API tests / integration test / Acceptance tests](documents/ACCEPTANCE_TESTS.md)
- [Application Configuration Reference](documents/APPLICATION_CONFIGURATION_REFERENCE.md)
- [Task Differences](documents/TASK_DIFFERENCIES.md)
- [Known Limitation and Improvements](documents/Limitation%20and%20Improvements.md)