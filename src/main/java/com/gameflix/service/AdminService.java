package com.gameflix.service;

import com.gameflix.dto.AdminStatsDto;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UserDto;
import com.gameflix.entity.Game;
import com.gameflix.entity.Role;
import com.gameflix.entity.SubscriptionStatus;
import com.gameflix.entity.User;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.mapper.EntityMapper;
import com.gameflix.repository.GameRepository;
import com.gameflix.repository.SubscriptionRepository;
import com.gameflix.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final GameRepository gameRepository;

    public AdminService(
            UserRepository userRepository,
            SubscriptionRepository subscriptionRepository,
            GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.gameRepository = gameRepository;
    }

    public List<UserDto> listUsers() {
        log.info("Entering listUsers");
        List<UserDto> users = userRepository.findAll().stream()
                .map(EntityMapper::toUserDto)
                .toList();
        log.info("Exiting listUsers count={}", users.size());
        return users;
    }

    public UserDto getUser(Long id) {
        log.info("Entering getUser id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserDto dto = EntityMapper.toUserDto(user);
        log.info("Exiting getUser id={}", id);
        return dto;
    }

    @Transactional
    public UserDto updateUserRole(Long id, Role role) {
        log.info("Entering updateUserRole id={}, role={}", id, role);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setRole(role);
        User saved = userRepository.save(user);
        UserDto dto = EntityMapper.toUserDto(saved);
        log.info("Exiting updateUserRole id={}", id);
        return dto;
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Entering deleteUser id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        subscriptionRepository.findByUser(user).ifPresent(subscriptionRepository::delete);
        userRepository.delete(user);
        log.info("Exiting deleteUser id={}", id);
    }

    public List<SubscriptionDto> listSubscriptions() {
        log.info("Entering listSubscriptions");
        return subscriptionRepository.findAll().stream()
                .map(EntityMapper::toSubscriptionDto)
                .toList();
    }

    public AdminStatsDto getStats() {
        log.info("Entering getStats");
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalUsers(userRepository.count());
        stats.setActiveSubscriptions(subscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE));
        stats.setTotalGames(gameRepository.count());

        BigDecimal revenue = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE).stream()
                .map(sub -> sub.getPlan().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setMonthlyRevenue(revenue);

        List<AdminStatsDto.PopularGameDto> popularGames = gameRepository.findAll(PageRequest.of(0, 5))
                .getContent().stream()
                .sorted(Comparator.comparingDouble(Game::getRating).reversed())
                .limit(5)
                .map(EntityMapper::toPopularGameDto)
                .toList();
        stats.setPopularGames(popularGames);

        log.info("Exiting getStats");
        return stats;
    }
}
