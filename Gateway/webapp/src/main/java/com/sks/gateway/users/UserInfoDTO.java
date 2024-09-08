package com.sks.gateway.users;

import com.sks.users.api.UserDTO;

import java.security.Principal;

/**
 * Data Transfer Object (DTO) that encapsulates user information.
 * This record holds the internal user details and the security principal.
 *
 * @param internalUser the internal user details
 * @param principal the security principal
 */
public record UserInfoDTO(UserDTO internalUser, Principal principal) {
}