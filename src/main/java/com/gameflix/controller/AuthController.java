package com.gameflix.controller;
import com.gameflix.dto.ApiResponse;
import com.gameflix.dto.JwtResponse;
import com.gameflix.dto.LoginRequest;
import com.gameflix.dto.RefreshTokenRequest;
import com.gameflix.dto.RegisterRequest;
import com.gameflix.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(@Valid @RequestBody RegisterRequest request) {
        JwtResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Registration successful"));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Login successful"));
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        authService.logout(token);
        return ResponseEntity.ok(ApiResponse.ok(null, "Logout successful"));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        JwtResponse response = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.ok(response, "Token refreshed"));
    }
}
