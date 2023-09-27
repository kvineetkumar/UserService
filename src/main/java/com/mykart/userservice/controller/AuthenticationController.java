package com.mykart.userservice.controller;


import com.mykart.userservice.config.JwtUtil;
import com.mykart.userservice.dto.AuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping(value = "auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            authenticationManager.authenticate(authentication);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        final UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        if (user != null) {
            return ResponseEntity.ok(jwtUtil.generateToken(user));
        } else {
            return ResponseEntity.internalServerError().body("Some error has occurred");
        }
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "You secured with JWT Token as ADMIN.";
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "You secured with JWT Token as USER.";
    }

    @GetMapping("public")
    public String publicData() {
        return "Free access to public data.";
    }

    @GetMapping("public/greeting")
    public String greeting() {
        return "Greetings!";
    }
}