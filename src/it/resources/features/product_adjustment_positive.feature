Feature: Test feature Hello World

  Scenario Outline: Add new product adjustment for exist product
    When PW: send request to add prise adjustment for product with id [<PRODUCT_ID>]
    """json
    {
      "productId": <PRODUCT_ID>,
      "mode": "ABSOLUTE",
      "type": "FEE",
      "value": 1234
    }
    """
    Then response status should be 200 with JSON content type
    And response body is equal to json
    """json
    {}
    """
    Examples:
      | PRODUCT_ID |
      | 1          |