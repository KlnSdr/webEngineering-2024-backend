package com.sks.gateway.users;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
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

    public UsersResource(UsersSender sender) {
        this.sender = sender;
    }

    @GetMapping("/current")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }

    @GetMapping("/id/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        final UsersResponseMessage responseMessage = sender.sendRequest(UsersRequestMessage.findUser(id));

        final UserDTO user = responseMessage.getUser();

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }

        return user;
    }
}
