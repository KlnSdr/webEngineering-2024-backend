package com.sks.gateway.users;

import com.sks.users.api.UserDTO;

import java.security.Principal;

public record UserInfoDTO(UserDTO internalUser, Principal principal) {
}
