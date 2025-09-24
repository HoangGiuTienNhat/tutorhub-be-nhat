package com.yourcompany.yourproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to TutorHub!";
    }
}
