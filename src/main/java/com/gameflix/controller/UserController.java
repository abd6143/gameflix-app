package com.gameflix.controller;

import com.gameflix.dto.ApiResponse;
import com.gameflix.dto.PasswordChangeRequest;
import com.gameflix.dto.UserDto;
import com.gameflix.service.UserService;
import com.gameflix.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    public UserController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getProfile() {
        UserDto user = userService.getCurrentUser(securityUtils.getCurrentUserEmail());
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(@Valid @RequestBody UserDto update) {
        UserDto user = userService.updateProfile(securityUtils.getCurrentUserEmail(), update);
        return ResponseEntity.ok(ApiResponse.ok(user, "Profile updated"));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(securityUtils.getCurrentUserEmail(), request);
        return ResponseEntity.ok(ApiResponse.ok(null, "Password changed"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteAccount() {
        userService.deleteAccount(securityUtils.getCurrentUserEmail());
        return ResponseEntity.ok(ApiResponse.ok(null, "Account deleted"));
    }
}
