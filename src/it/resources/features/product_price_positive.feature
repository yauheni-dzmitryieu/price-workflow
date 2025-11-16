Feature: Check getting product price by product id


  Scenario Outline: Get product price by product id
    When PW: send request to get product price by productId [<PRODUCT_ID>]
    Then response status should be 200 with JSON content type
    And response body is equal to json
    """json
    {
      "productId" : 1,
      "basePrice": 1,
      "priceAdjustments": [

      ],
      "finalPrice": 123
    }
    """
    Examples:
      | PRODUCT_ID |
      | 1          |