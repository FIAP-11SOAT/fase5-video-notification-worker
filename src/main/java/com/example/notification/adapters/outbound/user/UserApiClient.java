package com.example.notification.adapters.outbound.user;

import com.example.notification.adapters.outbound.dto.UserResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "userClient",
        url = "${auth.service.url}"
)
public interface UserApiClient {

    @GetMapping("/auth/user-by-id/{userId}")
    @Headers("X-MS-Token: dXMtZWFzdC0xX0xPamx4NWIzSDo3aHMxaG80aXYybTYxYmptYmNmMzRtZnFs")
    UserResponse getUserById(@PathVariable String userId);
}