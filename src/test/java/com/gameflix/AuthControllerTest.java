package com.gameflix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameflix.dto.LoginRequest;
import com.gameflix.dto.RegisterRequest;
import com.gameflix.entity.Role;
import com.gameflix.entity.User;
import com.gameflix.repository.UserRepository;
import com.gameflix.service.TokenBlocklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenBlocklistService tokenBlocklistService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_withValidData_returnsJwt() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newgamer");
        request.setEmail("new@gameflix.com");
        request.setPassword("Password@123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    void register_withDuplicateEmail_returns409() throws Exception {
        User existing = new User();
        existing.setUsername("existing");
        existing.setEmail("dup@gameflix.com");
        existing.setPassword(passwordEncoder.encode("Password@123"));
        existing.setRole(Role.USER);
        userRepository.save(existing);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("another");
        request.setEmail("dup@gameflix.com");
        request.setPassword("Password@123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@gameflix.com");
        user.setPassword(passwordEncoder.encode("Correct@123"));
        user.setRole(Role.USER);
        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gameflix.com");
        request.setPassword("Wrong@123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        User user = new User();
        user.setUsername("validuser");
        user.setEmail("valid@gameflix.com");
        user.setPassword(passwordEncoder.encode("Valid@123"));
        user.setRole(Role.USER);
        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("valid@gameflix.com");
        request.setPassword("Valid@123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }
}
