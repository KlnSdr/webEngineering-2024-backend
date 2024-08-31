package com.sks.gateway.util;

import com.sks.users.api.UserDTO;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Component for verifying user access.
 */
@Component
public class AccessVerifier {
    private final UserHelper userHelper;

    /**
     * Constructs an AccessVerifier with the specified UserHelper.
     *
     * @param userHelper the helper to use for user operations
     */
    public AccessVerifier(UserHelper userHelper) {
        this.userHelper = userHelper;
    }

    /**
     * Verifies if the current user has access to their own data/is accessing their own data.
     *
     * @param targetUserId the ID of the target user
     * @param principal the security principal of the current user
     * @return true if the current user has access to their own data, false otherwise
     */
    public boolean verifyAccessesSelf(long targetUserId, Principal principal) {
        if (principal == null) {
            return false;
        }

        final UserDTO user = userHelper.getCurrentInternalUser(principal);
        return user != null && user.getUserId() == targetUserId;
    }
}