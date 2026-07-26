package com.gameflix.service;

import com.gameflix.dto.GameDto;
import com.gameflix.dto.PasswordChangeRequest;
import com.gameflix.dto.UserDto;
import com.gameflix.entity.User;
import com.gameflix.exception.ConflictException;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.exception.UnauthorizedException;
import com.gameflix.mapper.EntityMapper;
import com.gameflix.repository.SubscriptionRepository;
import com.gameflix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            SubscriptionRepository subscriptionRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto getCurrentUser(String email) {
        log.info("Entering getCurrentUser for email={}", email);
        User user = findByEmail(email);
        UserDto dto = EntityMapper.toUserDto(user);
        log.info("Exiting getCurrentUser for email={}", email);
        return dto;
    }

    @Transactional
    public UserDto updateProfile(String email, UserDto update) {
        log.info("Entering updateProfile for email={}", email);
        User user = findByEmail(email);

        if (!user.getUsername().equals(update.getUsername())
                && userRepository.existsByUsername(update.getUsername())) {
            throw new ConflictException("Username already taken");
        }
        if (!user.getEmail().equals(update.getEmail())
                && userRepository.existsByEmail(update.getEmail())) {
            throw new ConflictException("Email already registered");
        }

        user.setUsername(update.getUsername());
        user.setEmail(update.getEmail());
        userRepository.save(user);

        UserDto dto = EntityMapper.toUserDto(user);
        log.info("Exiting updateProfile for email={}", email);
        return dto;
    }

    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        log.info("Entering changePassword for email={}", email);
        User user = findByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Exiting changePassword for email={}", email);
    }

    @Transactional
    public void deleteAccount(String email) {
        log.info("Entering deleteAccount for email={}", email);
        User user = findByEmail(email);
        subscriptionRepository.findByUser(user).ifPresent(subscriptionRepository::delete);
        userRepository.delete(user);
        log.info("Exiting deleteAccount for email={}", email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
