# price-workflow

# Tasks
- [X] Project skeleton
- [X] Postgres connection
- [X] Cache L1
  - [X] Price adjustment
  - [X] Product
- [X] Cache L2
  - [X] Price adjustment
  - [X] Product
- [X] Add Redis to docker-compose
- [ ] Metrics
  - [X] Enable AspectJ
  - [X] Add metrics for DB
  - [ ] Add metrics for Redis
  - [ ] Add metrics to track failed requests
  - [X] Wrap requests
- [X] Add requestId /...
- [ ] Prometheus 
  - [X] Add to docker-compose
  - [ ] Configure dashboards
- [ ] Add logs
  - [X] Add log configuration
  - [X] Add exception handler
- [ ] Adjust logic to allow work stable in case Redis connection failed
- [X] Add app to docker-compose with 2 replica
- [ ] Update performance tests
- [X] Unit tests
- [ ] Integration tests
- [ ] Documentations
  - [X] API docs 
  - [ ] Architecture (schema, descriptions)

# Domains

## Product

| Field name | Type | Description |
|------------|------|-------------|
| id         |      |             |
| sku        |      |             |
| name       |      |             |
| basePrice  |      |             |

## PriceAdjustment

| Field name | Type | Description      |
|------------|------|------------------|
| id         |      |                  |
| productId  |      |                  |
| type       |      | PROMO, TAX, FEE  |
| value      |      |                  |
| mode       |      | ABSOLUTE,PERCENT |
| updatedAt  |      |                  |

# Questions

1. Do we need additional endpoints to be able?
   1. Add Product
   2. Update Product
   3. Delete Product
2. adjustments[] -> should contains full fields from PriceAdjustment
3. Format for currency?
5. Expected count of request + parallel users for:
   * p95 for frequently requested items < 10 ms 
   * p99 < 20 ms
6. Do we have some requirements for the p95, p99 without L1, L2 cache just DB

# Improvements

GET /price/{productId} -> GET /api/v1/products/{productId}/price
POST /admin/price/{productId}/adjustments -> POST /api/v1/admin/products/{productId}/adjustments