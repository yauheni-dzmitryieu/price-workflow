package com.yd.price.workflow.rest.api;

import com.yd.price.workflow.model.ErrorResponse;
import com.yd.price.workflow.model.ProductPrice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Product requests")
public interface ProductApi {

    @Operation(summary = "Get product price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductPrice.class),
                            examples = {
                                    @ExampleObject(name = "Successful operation",
                                            value = """
                                                            {
                                                              "productId": 101004,
                                                              "basePrice": 100.0000,
                                                              "priceAdjustments": [
                                                                {
                                                                  "id": 8685,
                                                                  "productId": 101004,
                                                                  "type": "FEE",
                                                                  "value": 10.00,
                                                                  "mode": "ABSOLUTE",
                                                                  "updatedAt": "2025-11-20T23:22:35.899571"
                                                                },
                                                                {
                                                                  "id": 8674,
                                                                  "productId": 101004,
                                                                  "type": "PROMO",
                                                                  "value": 10.00,
                                                                  "mode": "PERCENT",
                                                                  "updatedAt": "2025-11-19T17:51:47.93921"
                                                                }
                                                              ],
                                                              "finalPrice": 120
                                                            }
                                                    """)

                            })),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = """
                                            {
                                              "errorCode": "ERROR_CODE",
                                              "errorDescription": "Error description"
                                            }
                                            """)
                            })
            ),
            @ApiResponse(responseCode = "500", description = "Unexpected error. Possible error codes [INTERNAL_SERVER_ERROR]",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = """
                                            {
                                              "errorCode": "ERROR_CODE",
                                              "errorDescription": "Error description"
                                            }
                                            """)
                            })
            )
    })
    ResponseEntity<ProductPrice> getPrice(@Parameter(in = ParameterIn.PATH, required = true, name = "productId") Long productId);
}
