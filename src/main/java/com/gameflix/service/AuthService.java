package com.gameflix.service;

import com.gameflix.dto.JwtResponse;
import com.gameflix.dto.LoginRequest;
import com.gameflix.dto.RegisterRequest;
import com.gameflix.entity.Role;
import com.gameflix.entity.User;
import com.gameflix.exception.ConflictException;
import com.gameflix.exception.UnauthorizedException;
import com.gameflix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public JwtResponse register(RegisterRequest request) {
        log.info("Entering register for email={}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        JwtResponse response = buildTokenResponse(user);
        log.info("Exiting register for email={}", request.getEmail());
        return response;
    }

    public JwtResponse login(LoginRequest request) {
        log.info("Entering login for email={}", request.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception ex) {
            log.warn("Login failed for email={}", request.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        JwtResponse response = buildTokenResponse(user);
        log.info("Exiting login for email={}", request.getEmail());
        return response;
    }

    public void logout(String token) {
        log.info("Entering logout");
        if (token != null && token.startsWith("Bearer ")) {
            jwtService.invalidateToken(token.substring(7));
        } else if (token != null) {
            jwtService.invalidateToken(token);
        }
        log.info("Exiting logout");
    }

    public JwtResponse refresh(String refreshToken) {
        log.info("Entering refresh");
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user.getEmail())) {
            throw new UnauthorizedException("Refresh token expired or invalid");
        }

        JwtResponse response = buildTokenResponse(user);
        log.info("Exiting refresh for email={}", user.getEmail());
        return response;
    }

    private JwtResponse buildTokenResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole());
        return new JwtResponse(accessToken, refreshToken, jwtService.getAccessTokenExpirationMs() / 1000);
    }
}
