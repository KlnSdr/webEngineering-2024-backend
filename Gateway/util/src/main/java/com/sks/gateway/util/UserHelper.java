package com.sks.gateway.util;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Helper component for user-related operations.
 */
@Component
public class UserHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);
    private final UsersSender usersSender;

    /**
     * Constructs a UserHelper with the specified UsersSender.
     *
     * @param usersSender the sender to use for sending user requests
     */
    public UserHelper(UsersSender usersSender) {
        this.usersSender = usersSender;
    }

    /**
     * Retrieves the current internal user based on the provided security principal.
     *
     * @param principal the security principal of the current user
     * @return the UserDTO of the current internal user, or null if the principal is null
     */
    public UserDTO getCurrentInternalUser(Principal principal) {
        if (principal == null) {
            return null;
        }

        final String idpUserId = principal.getName();
        final Long userId = Long.parseLong(idpUserId);
        final UsersResponseMessage response = usersSender.sendRequest(UsersRequestMessage.findUserIdp(userId));

        if (response.didError()) {
            LOGGER.error("Error while getting current user for idpUserId: {}", idpUserId);
            LOGGER.trace(response.getErrorMessage(), response.getException());
            return null;
        }

        return response.getUser();
    }
}