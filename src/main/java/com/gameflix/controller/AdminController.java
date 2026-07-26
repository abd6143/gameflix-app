package com.gameflix.controller;
import com.gameflix.dto.AdminStatsDto;
import com.gameflix.dto.ApiResponse;
import com.gameflix.dto.RoleUpdateRequest;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UserDto;
import com.gameflix.service.AdminService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> listUsers() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.listUsers()));
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getUser(id)));
    }
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserDto>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request) {
        UserDto user = adminService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok(ApiResponse.ok(user, "Role updated"));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "User deleted"));
    }
    @GetMapping("/subscriptions")
    public ResponseEntity<ApiResponse<List<SubscriptionDto>>> listSubscriptions() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.listSubscriptions()));
    }
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsDto>> getStats() {
        return ResponseEntity.ok(ApiResponse.ok(adminService.getStats()));
    }
}
