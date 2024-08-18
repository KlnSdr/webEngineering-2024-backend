package com.sks.gateway.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UsersResource {
    @GetMapping("/current")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }
}
