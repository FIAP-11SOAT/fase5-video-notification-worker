package com.example.notification.adapters.outbound.user;

import com.example.notification.adapters.outbound.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
public class UserAdapter implements UserOutboundPort {

    private final UserApiClient client;

    public UserAdapter(UserApiClient client) {
        this.client = client;
    }

    @Override
    public UserResponse getUserById(String userId) {
        try {
            return client.getUserById(userId);
        } catch (Exception e){
            log.error(
                    "Error getting user",
                    kv("class", "UserAdapter"),
                    kv("userId", userId),
                    e
            );
            throw new RuntimeException("Error getting user", e);
        }
    }
}

