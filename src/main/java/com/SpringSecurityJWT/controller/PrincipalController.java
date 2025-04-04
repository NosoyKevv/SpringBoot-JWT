package com.SpringSecurityJWT.controller;

import com.SpringSecurityJWT.Dto.CreateUser;
import com.SpringSecurityJWT.models.ERole;
import com.SpringSecurityJWT.models.RoleEntity;
import com.SpringSecurityJWT.models.UserEntity;
import com.SpringSecurityJWT.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PrincipalController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public PrincipalController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring NOT Secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured() {
        return "Hello Spring Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUser request) {

        Set<RoleEntity> roles = request.getRoles().stream().map(role -> RoleEntity
                        .builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(userEntity);
        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));
        return "User deleted ".concat(id);
    }
}
