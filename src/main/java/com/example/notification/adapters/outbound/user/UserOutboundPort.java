package com.example.notification.adapters.outbound.user;

import com.example.notification.adapters.outbound.dto.UserResponse;

public interface UserOutboundPort {
    UserResponse getUserById(String userId);
}
