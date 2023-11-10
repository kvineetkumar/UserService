package com.mykart.userservice.model.auth;

public record AuthenticationRequest(String email, String password) {
}