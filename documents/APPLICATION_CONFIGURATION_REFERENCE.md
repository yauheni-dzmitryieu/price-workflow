# Application Configuration Reference

| Property Path                                 | Description                                                                              | Default Value                               |
|-----------------------------------------------|------------------------------------------------------------------------------------------|---------------------------------------------|
| **spring.profiles.active**                    | Active Spring profile used when running the application.                                 | `dev`                                       |
| **spring.redis.redisson.file**                | Path to the Redisson configuration file (YAML), loaded from the classpath.               | `classpath:redisson.yml`                    |
| **spring.datasource.url**                     | JDBC URL pointing to the PostgreSQL database instance.                                   | `jdbc:postgresql://localhost:5432/postgres` |
| **spring.datasource.username**                | Username for connecting to the PostgreSQL database.                                      | `postgres`                                  |
| **spring.datasource.password**                | Password for connecting to the PostgreSQL database.                                      | `postgres`                                  |
| **spring.datasource.driver-class-name**       | Fully-qualified class name of the JDBC driver.                                           | `org.postgresql.Driver`                     |
| **spring.flyway.enabled**                     | Enables Flyway database migration on application startup.                                | `true`                                      |
| **spring.flyway.locations**                   | Classpath locations where Flyway migration scripts are stored.                           | `classpath:db/migration`                    |
| **spring.config.import**                      | Imports external configuration file with application error messages.                     | `classpath:error-messages.yml`              |
| **springdoc.swagger-ui.use-root-path**        | Exposes Swagger UI on the root path `/` instead of `/swagger-ui`.                        | `true`                                      |
| **logging.level.root**                        | Base logging level for the entire application runtime.                                   | `INFO`                                      |
| **logging.level.com.yd.price.workflow**       | Logging level specifically for your application package.                                 | `DEBUG`                                     |
| **management.endpoints.web.exposure.include** | Actuator endpoints exposed over HTTP.                                                    | `health,info,metrics,prometheus`            |
| **management.health.redis.enabled**           | Enables/disables Redis health check reporting. Disabled commonly in failover setups.     | `false`                                     |
| **management.health.db.enabled**              | Enables database health check monitoring.                                                | `true`                                      |
| **management.endpoint.health.show-details**   | Controls how much detail is shown in the health endpoint. `always` exposes full details. | `always`                                    |
| **mybatis.mapper-locations**                  | Location of MyBatis mapper XML files.                                                    | `classpath:mappers/*.xml`                   |
| **mybatis.type-aliases-package**              | Package containing MyBatis type aliases for entity classes.                              | `com.yd.price.workflow.persistence.entity`  |
| **server.port**                               | TCP port on which the Spring Boot application listens.                                   | `8087`                                      |


## L1 Cache (In-Memory, Local Cache)

| Property Path                                                     | Description                                                   | Default Value |
|-------------------------------------------------------------------|---------------------------------------------------------------|---------------|
| **app.cache.l1.enabled**                                          | Enables or disables local in-memory L1 caching (Caffeine).    | `true`        |
| **app.cache.l1.caches.product-cache.expire-after-write**          | TTL for product cache entries after being written (Caffeine). | `10m`         |
| **app.cache.l1.caches.product-cache.maximum-size**                | Maximum number of cached product entries in L1 cache.         | `10_000`      |
| **app.cache.l1.caches.price-adjustment-cache.expire-after-write** | TTL for price adjustments in L1 cache.                        | `10m`         |
| **app.cache.l1.caches.price-adjustment-cache.maximum-size**       | Maximum number of price adjustment entries.                   | `10_000`      |



## L2 Cache (Distributed, Redis-Based Cache)

| Property Path                                         | Description                                             | Default Value |
|-------------------------------------------------------|---------------------------------------------------------|---------------|
| **app.cache.l2.enabled**                              | Controls whether L2 (Redis) caching is active.          | `true`        |
| **app.cache.l2.scheduler-health-check-fixed-rate**    | Interval (in seconds) for Redis health-check scheduler. | `1`           |
| **app.cache.l2.caches.product-cache.expire**          | Time-to-live for product cache entries in Redis.        | `1d`          |
| **app.cache.l2.caches.price-adjustment-cache.expire** | TTL for price adjustment entries in Redis.              | `1d`          |

