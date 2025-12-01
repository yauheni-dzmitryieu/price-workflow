# Project Architecture
<!-- TOC -->
* [Project Architecture](#project-architecture)
  * [Write data flow (Write through* strategy)](#write-data-flow-write-through-strategy)
  * [Get data flow](#get-data-flow)
    * [Read from Caches(L1, L2) + L1 data missed](#read-from-cachesl1-l2--l1-data-missed-)
    * [Read from Caches(L1, L2) and DB if data missed](#read-from-cachesl1-l2-and-db-if-data-missed)
  * [Infrastructure](#infrastructure)
    * [Redis](#redis-)
      * [Sentinel-Based Discovery](#sentinel-based-discovery)
      * [Failover Handling](#failover-handling)
    * [Monitoring](#monitoring)
    * [Application Layer](#application-layer)
<!-- TOC -->

## Write data flow (Write through* strategy)
![](./images/write_to_cache.png)
 
Steps 3 and 4 won’t be executed if the data isn’t saved to the database.

## Get data flow
### Read from Caches(L1, L2) + L1 data missed 
![](./images/read_from_L1_L2.png)

### Read from Caches(L1, L2) and DB if data missed
![](./images/read_and_update_cache.png)

## Infrastructure
### Redis 
![](./images/redis_sentinel_schema.png)

The **price-workflow** service connects to Redis through a Redis Sentinel cluster, ensuring high availability and automatic failover.
 
#### Sentinel-Based Discovery

The application does not connect directly to Redis master/slaves.
Instead, it communicates with Sentinel nodes, which:

* Monitor the Redis master.
* Detect failures.
* Elect a new master if the current master goes down.
* Notify clients (including price-workflow) about topology changes.

#### Failover Handling
If the master becomes unavailable:

1. Sentinel performs a failover, promoting one slave to master.
2. Sentinel notifies the application.
3. price-workflow automatically reconnects to the new master without restart.

### Monitoring
![](images/monitoring_schema.png)

```
                           ┌──────────────────────────────┐
                           │            Clients           │
                           └──────────────┬───────────────┘
                                          │  HTTP :80
                                          ▼
                         ┌──────────────────────────────────────┐
                         │              Traefik                 │
                         │  - Reverse proxy                     │
                         │  - Router for price-workflow         │
                         │  - Dashboard :8080                   │
                         │  - Metrics (Prometheus) :8082        │
                         └───────────────┬──────────────────────┘
                                         │
       ┌─────────────────────────────────┼──────────────────────────────────┐
       │                                 │                                  │
       ▼                                 ▼                                  ▼

┌────────────────┐        ┌────────────────────────────────────┐      ┌────────────────┐
│  Grafana       │        │          Prometheus                │      │  Socket Proxy  │
│  UI :3000      │ <────> │   Scrapes metrics from Traefik     │      │ Controls access│
│                │        │   and price-workflow replicas      │      │ to docker.sock │
└────────────────┘        └────────────────────────────────────┘      └────────────────┘
```

###  Application Layer
```
                         ┌───────────────────────────────────┐
                         │     price-workflow (Replica 1)    │
                         ├───────────────────────────────────┤
                         │     price-workflow (Replica 2)    │
                         └───────────────────────────────────┘
                                   ▲          ▲
                                   │          │
                                   │          │
                                   │          │
                                   └─────┬────┘
                                         │
                                   Load Balancing
                                         │
                                         ▼
                                  Handled by Traefik
```

## Database Schema
```mermaid
erDiagram
    PRODUCT ||--o{ PRICE_ADJUSTMENT: "has"

    PRODUCT {
        id bigserial PK "Primary Key"
        sku varchar_64 "Unique Stock Keeping Unit"
        name varchar_255 "Product Name"
        base_price numeric_19_2 "Base Price"
    }

    PRICE_ADJUSTMENT {
        id bigserial PK "Primary Key"
        product_id bigint FK "FK to PRODUCT (ON DELETE CASCADE)"
        type price_adjustment_type "PROMO, TAX, or FEE"
        value numeric_19_2 "Adjustment value"
        price_adjustment_mode mode "ABSOLUTE or PERCENT"
        updated_at timestamp "Time of last update"
    }
```