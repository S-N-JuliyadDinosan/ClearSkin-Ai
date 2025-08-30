package com.clearskinai.adminservice.client.feignClients;

import com.clearskinai.adminservice.client.clientDtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "userservice",
        url = "http://localhost:8082",
        path = "/api/v1/user"
)
public interface UserClient {

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/email")
    UserDto getUserByEmail(@RequestParam("email") String email);
}
