package com.mykart.userservice.controller;

import com.mykart.userservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/")
public class AdminController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PatchMapping("role/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> updateUserRole(@PathVariable("username") int userId, @RequestParam("admin") boolean isAdmin) {
        String response = authenticationService.updateUserRole(userId, isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
