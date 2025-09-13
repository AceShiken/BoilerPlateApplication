package com.demo.feign;

import com.demo.dto.CreateUserRequest;
import com.demo.entity.User;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "user-service-client",
        url = "${feign.userService.base-url}",
        configuration = com.demo.config.OpenFeignConfig.class
)
public interface UserServiceClient {

    @PostMapping("/user/add")
    @Headers("Content-Type: application/json")
    User addUser(@RequestBody CreateUserRequest req);

    @GetMapping("/user/get")
    User getUser(@RequestParam("id") Long id);
}