package com.sks.gateway.util;

import com.sks.users.api.UserDTO;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AccessVerifier {
    private final UserHelper userHelper;

    public AccessVerifier(UserHelper userHelper) {
        this.userHelper = userHelper;
    }

    public boolean verifyAccessesSelf(long targetUserId, Principal principal) {
        if (principal == null) {
            return false;
        }

        final UserDTO user = userHelper.getCurrentInternalUser(principal);
        return user != null && user.getUserId() == targetUserId;
    }
}
