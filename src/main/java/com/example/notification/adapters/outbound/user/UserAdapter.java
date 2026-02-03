package com.example.notification.adapters.outbound.user;

import com.example.notification.adapters.outbound.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter implements UserOutboundPort {

    private final UserApiClient client;

    public UserAdapter(UserApiClient client) {
        this.client = client;
    }

    @Override
    public UserResponse getUserById(String userId) {
        return client.getUserById(userId);
    }
}

