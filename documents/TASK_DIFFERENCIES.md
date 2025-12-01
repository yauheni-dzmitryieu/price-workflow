# Differences between the original assignment and the implementation

## API paths
GET /price/{productId} -> GET /api/v1/products/{productId}/price
POST /admin/price/{productId}/adjustments -> POST /api/v1/admin/products/{productId}/adjustments

## Final price calculations and storage
### Source
Final price: basePrice + sum(ABSOLUTE) + sum(PERCENT * basePrice)

### Implemented
Final price: basePrice + sum(ABSOLUTE) + sum((PERCENT * basePrice) / 100)



