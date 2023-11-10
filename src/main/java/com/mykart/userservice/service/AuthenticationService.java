package com.mykart.userservice.service;

import com.mykart.userservice.config.JwtUtil;
import com.mykart.userservice.model.auth.AuthenticationRequest;
import com.mykart.userservice.model.auth.AuthenticationResponse;
import com.mykart.userservice.model.auth.RegisterRequest;
import com.mykart.userservice.model.user.AppUser;
import com.mykart.userservice.model.user.Role;
import com.mykart.userservice.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = AppUser.builder()
                .firstName(registerRequest.firstname())
                .lastName(registerRequest.lastname())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.email(),
                            authenticationRequest.password()
                    )
            );
        } catch (Exception ex) {
            log.debug(ex);
        }

        var user = userRepository.findByEmail(authenticationRequest.email())
                .orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public String updateUserRole(int userId, boolean isAdmin) {
        Optional<AppUser> userOptional = userRepository.findById(userId);
        AppUser appUser = userOptional.map(user -> {
            user.setRole(isAdmin ? Role.ADMIN : Role.USER);
            return userRepository.save(user);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return appUser.getFirstName().concat(" is now " + appUser.getRole().name());
    }
}
