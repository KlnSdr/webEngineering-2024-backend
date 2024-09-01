package com.sks.gateway.users;

import com.sks.gateway.common.MessageErrorHandler;
import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UsersResource {
    final UsersSender sender;
    final MessageErrorHandler messageErrorHandler;

    public UsersResource(UsersSender sender, MessageErrorHandler messageErrorHandler) {
        this.sender = sender;
        this.messageErrorHandler = messageErrorHandler;
    }

    @Operation(summary = "Get the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the current user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Principal.class))),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @GetMapping("/current")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to send/receive message to/from service", content = @Content)
    })
    @GetMapping("/id/{id}")
    public UserDTO getUserById(
            @Parameter(description = "ID of the user to be fetched") @PathVariable("id") Long id) {
        final UsersResponseMessage responseMessage = sender.sendRequest(UsersRequestMessage.findUser(id));

        if (responseMessage.didError()) {
            messageErrorHandler.handle(responseMessage);
        }


        final UserDTO user = responseMessage.getUser();

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }

        return user;
    }
}