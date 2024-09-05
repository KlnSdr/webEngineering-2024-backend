package com.sks.gateway.common;

import com.sks.users.api.UserDTO;
import com.sks.users.api.UsersRequestMessage;
import com.sks.users.api.UsersResponseMessage;
import com.sks.users.api.UsersSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomeUserDetailsService implements UserDetailsService {
    private final UsersSender usersSender;

    public CustomeUserDetailsService(UsersSender usersSender) {
        this.usersSender = usersSender;
    }

    @Override
    public UserDetails loadUserByUsername(String idpUserId) throws UsernameNotFoundException {
        long userId = Long.parseLong(idpUserId);
        final UsersResponseMessage response = usersSender.sendRequest(UsersRequestMessage.findUserIdp(userId));

        if (response.didError()) {
            return null;
        }
        final UserDTO user = response.getUser();

        if (user == null) {
            return null;
        }

        return User.builder()
                .username(user.getUserName())
                .password("")
                .build();
    }
}
