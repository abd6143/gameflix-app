package com.gameflix.config;

import com.gameflix.security.JwtAuthFilter;
import com.gameflix.security.RateLimitFilter;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            RateLimitFilter rateLimitFilter,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/subscriptions/plans").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"errorCode\":\"UNAUTHORIZED\",\"message\":\"Authentication required\"}");
                        }))
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        List<String> publicPaths = Arrays.asList(
                "/", "/login", "/register", "/catalog", "/games/**",
                "/css/**", "/js/**", "/images/**", "/error",
                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                "/h2-console/**");

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/**"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicPaths.stream()
                                .map(AntPathRequestMatcher::new)
                                .toArray(AntPathRequestMatcher[]::new))
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/dashboard", "/subscription", "/profile/**").authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .permitAll())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
