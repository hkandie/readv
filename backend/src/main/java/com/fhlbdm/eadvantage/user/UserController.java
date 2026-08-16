package com.fhlbdm.eadvantage.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fhlbdm.eadvantage.user.models.UserResponse;

@RestController
public class UserController {

    @GetMapping("/api/user/me")
    public UserResponse getCurrentUser() {
        return new UserResponse("1", "Ada Lovelace", "ada@example.com");
    }
}
