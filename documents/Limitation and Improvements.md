# Known limitations and possible solutions

## LM1: Inconsistent data after restoring connection with L2 cache(Redis) in L1 cache (all app instances lose connection with L2 but L2 working and storing old data) + apps continue work via DB
### Problem
L1 + L2 Caches will contain inconsistent data after losing connection with L2 cache

### Solution
1. Add try cache block for each operation with L2 cache
   1. if L2 will unavailable switch health status per instance to `Unavailable` and work with DB directly avoid L1, L2 operations
2. Evict (Clean) L1
3. In Scheduler check health (store connection statuses: Available, Unavailable, Restoring),
   * Restoring: Evict (Clean) L2 -> switch flag to Available
4. L1 + L2 will be automatically filled per each request

## L2: 
### Problem
TBD

### Solution
TBD

# Improvements
## 1. Add Retry logic for DB requests
## 2. Add Retry logic for L2 requests
## 3. Update DB models
* Add version fields -> required for consistent data working with distributed system
* Add created timestamp -> for audit
## 4. Improve unit test coverage
## 5. Add Acceptance tests (Cucumber)
## 6. Add Spring security
* Limit operation that required Admin access user Roles and privileges
## 7. Configure secure environments, required Traefik configuration 
## 8. Add mutation tests to check quality of unit tests
## 9. Split project to modules
- domain
- database
- repository
- api
- rest-api
- service
- performance-tests
- acceptance-tests
## 10. Improve metrics
* Add metrics for each L2 request
* Track exception with L2, DB, REST API
## 11. Improve performance tests
* Add mix operations
### 12. Improve logging
### 13. Add working config for Grafana
* Prometheus
* Dashboards