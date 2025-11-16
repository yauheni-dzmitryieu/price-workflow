package com.yd.price.workflow.rest.api;

import com.yd.price.workflow.model.PriceAdjustment;
import com.yd.price.workflow.model.ProductPrice;
import com.yd.price.workflow.rest.dto.ProductPriceResponse;
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

@Tag(
        name = "Admin pricing",
        description = "Admin endpoints to manage price adjustments"
)
public interface ProductApi {

    @Operation(summary = "Get product price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PriceAdjustment.class), examples = {
                            @ExampleObject(name = "Successful operation", value = """
                                    {
                                    "productId": 1,
                                    }
                                    """)
                    })),
    })
    ResponseEntity<ProductPrice> getPrice(@Parameter(in = ParameterIn.PATH, required = true, name = "productId") Long productId);
}
