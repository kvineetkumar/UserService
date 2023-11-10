package com.mykart.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class UnsecuredApiController {

    @GetMapping("home")
    public String home() {
        return "You are not secured!!!";
    }

    @GetMapping
    public String greeting() {
        return "Greetings!";
    }
}
