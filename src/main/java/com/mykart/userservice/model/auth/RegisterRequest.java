package com.mykart.userservice.model.auth;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
