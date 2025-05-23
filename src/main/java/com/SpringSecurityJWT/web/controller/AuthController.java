package com.SpringSecurityJWT.web.controller;

import com.SpringSecurityJWT.security.service.JwtService;
import com.SpringSecurityJWT.web.dto.AuthRequest;
import com.SpringSecurityJWT.web.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.createToken(user);
        return ResponseEntity.ok(token);
    }


    @GetMapping("/public-access")
    public String endPointIsPublicAccess() {
        return "public-access";
    }
}