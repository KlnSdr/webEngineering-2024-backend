package com.sks.gateway.util;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AccessVerifier {
    private final UsersSender usersSender;

    public AccessVerifier(UsersSender usersSender) {
        this.usersSender = usersSender;
    }

    public boolean verifyAccessesSelf(long targetUserId, Principal principal) {
        if (principal == null) {
            return false;
        }

        final String idpUserId = principal.getName();
        final Long userId = Long.parseLong(idpUserId);
        final UsersResponseMessage response = usersSender.sendRequest(UsersRequestMessage.findUserIdp(userId));
        final UserDTO user = response.getUser();
        return user != null && user.getUserId() == targetUserId;
    }
}
