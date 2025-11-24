package com.yd.price.workflow.rest.api;

import com.yd.price.workflow.model.ErrorResponse;
import com.yd.price.workflow.model.PriceAdjustment;
import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Product adjustment requests",
        description = "Admin endpoints to manage price adjustments"
)
public interface AdminPriceAdjustmentApi {

    @Operation(summary = "Add price adjustment for the product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PriceAdjustment.class),
                            examples = {
                                    @ExampleObject(name = "Successful operation",
                                            value = """
                                                    {
                                                      "id": 100,
                                                      "productId": 123,
                                                      "type": "PROMO",
                                                      "value": 1345,
                                                      "mode": "ABSOLUTE"
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
    ResponseEntity<PriceAdjustment> addAdjustment(@Parameter(in = ParameterIn.PATH, name = "productId", required = true) Long productId,
                                                  @RequestBody(required = true,
                                                          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                                  schema = @Schema(implementation = PriceAdjustmentRequest.class)))
                                                  PriceAdjustmentRequest priceAdjustmentRequest);
}
