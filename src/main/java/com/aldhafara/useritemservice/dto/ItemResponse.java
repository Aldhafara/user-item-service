package com.aldhafara.useritemservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ItemResponse(

        @Schema(description = "Unique identifier of the item", example = "6210b1a3-2499-446d-a687-cce010a49864")
        UUID id,

        @Schema(description = "Name of the item", example = "My item")
        String name) {
}
