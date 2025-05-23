package org.example.dam.controller;

import org.example.dam.dto.UserDTO;
import org.example.dam.model.Role;
import org.example.dam.model.User;
import org.example.dam.repository.UserRepository;
import org.example.dam.security.JwtUtils;
import org.example.dam.service.UserService;
import org.example.dam.dto.LoginRequestDTO;
import org.example.dam.dto.TokenResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtTokenService;

    public AuthController(UserService userService, UserRepository userRepo, AuthenticationManager authenticationManager, JwtUtils jwtTokenService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtTokenService.generateToken(user); // full user
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO request) {
        if (userService.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists with this email");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        userService.save(request, isAdmin);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO request) {
        if (userService.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists with this email");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        userService.save(request, isAdmin);
        return ResponseEntity.ok("User registered successfully");
    }
}
