package com.alaaet.youtubeclone.controller;

import com.alaaet.youtubeclone.service.UserRegistrationService;
import com.alaaet.youtubeclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;

    @GetMapping("/register")
    public String register(Authentication authentication){
        Jwt jwt = (Jwt)authentication.getPrincipal();
        userRegistrationService.registerUser(jwt.getTokenValue());
        return "User Registeration Successful";
    }

    @PostMapping("subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser(@PathVariable String userId){
        userService.subscribeUser(userId);
        return true;
    }
    @PostMapping("unsubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean unsubscribeUser(@PathVariable String userId){
        userService.unsubscribeUser(userId);
        return true;
    }
}
