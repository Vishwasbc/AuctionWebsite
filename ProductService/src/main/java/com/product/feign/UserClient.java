package com.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.product.dto.UserDTO;

@FeignClient("USERSERVICE")
public interface UserClient {
	@GetMapping("api/user/{username}")
	UserDTO getByUserName(@PathVariable String username);
}
