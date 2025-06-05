package com.aldhafara.useritemservice.controllers;

import com.aldhafara.useritemservice.dto.CreateItemRequest;
import com.aldhafara.useritemservice.dto.ItemResponse;
import com.aldhafara.useritemservice.entities.User;
import com.aldhafara.useritemservice.services.ItemService;
import com.aldhafara.useritemservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@Tag(name = "Item Controller")
public class ItemController {

    private final int defaultPageSize;
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(@Value("${pagination.defaultSize}") int defaultPageSize,
                          ItemService itemService,
                          UserService userService) {
        this.defaultPageSize = defaultPageSize;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Operation(summary = "Create a new item",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Item created successfull"
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic"
                    )
            })
    @PostMapping
    public ResponseEntity<Void> createItem(@RequestBody CreateItemRequest request,
                                           Principal principal) {
        User user = getAuthenticatedUser(principal);
        itemService.createItemForUser(user, request.name());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a list of current user's items",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Get a list of current user's items",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = ItemResponse.class),
                                    examples = @ExampleObject(
                                            value = "[{\"id\": \"6210b1a3-2499-446d-a687-cce010a49864\", \"name\": \"My item\"}," +
                                                    "{\"id\": \"a68a558f-3736-48c7-bab0-ab4b0872b1a4\", \"name\": \"My other item\"}]"
                                    )
                            )

                    ),
                    @ApiResponse(responseCode = "401",
                            description = "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic"
                    )
            })
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getItems(
            Principal principal,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        User user = getAuthenticatedUser(principal);
        int effectivePage = page != null ? page : 0;
        int effectiveSize = size != null ? size : defaultPageSize;
        PageRequest pageRequest = PageRequest.of(effectivePage, effectiveSize);
        Page<ItemResponse> itemPage = itemService.getItemsForUser(user, pageRequest)
                .map(item -> new ItemResponse(item.getId(), item.getName()));

        return ResponseEntity.ok(itemPage.getContent());
    }

    private User getAuthenticatedUser(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return userService.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId)
        );
    }
}
