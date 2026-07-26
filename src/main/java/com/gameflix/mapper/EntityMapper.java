package com.gameflix.mapper;

import com.gameflix.dto.AdminStatsDto;
import com.gameflix.dto.GameDto;
import com.gameflix.dto.PlanDto;
import com.gameflix.dto.SubscriptionDto;
import com.gameflix.dto.UserDto;
import com.gameflix.entity.Game;
import com.gameflix.entity.Subscription;
import com.gameflix.entity.SubscriptionPlan;
import com.gameflix.entity.User;
import java.util.ArrayList;
import java.util.List;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        if (user.getSubscription() != null) {
            dto.setSubscriptionStatus(user.getSubscription().getStatus());
            if (user.getSubscription().getPlan() != null) {
                dto.setPlanName(user.getSubscription().getPlan().getName());
            }
        }
        return dto;
    }

    public static GameDto toGameDto(Game game) {
        GameDto dto = new GameDto();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setGenre(game.getGenre());
        dto.setDescription(game.getDescription());
        dto.setDeveloper(game.getDeveloper());
        dto.setPublisher(game.getPublisher());
        dto.setReleaseYear(game.getReleaseYear());
        dto.setRating(game.getRating());
        dto.setCoverImageUrl(game.getCoverImageUrl());
        dto.setPlatforms(new ArrayList<>(game.getPlatforms()));
        dto.setAvailableOnPlan(game.getAvailableOnPlan());
        return dto;
    }

    public static Game toGameEntity(GameDto dto) {
        Game game = new Game();
        updateGameFromDto(game, dto);
        return game;
    }

    public static void updateGameFromDto(Game game, GameDto dto) {
        game.setTitle(dto.getTitle());
        game.setGenre(dto.getGenre());
        game.setDescription(dto.getDescription());
        game.setDeveloper(dto.getDeveloper());
        game.setPublisher(dto.getPublisher());
        game.setReleaseYear(dto.getReleaseYear());
        game.setRating(dto.getRating());
        game.setCoverImageUrl(dto.getCoverImageUrl());
        game.setPlatforms(new ArrayList<>(dto.getPlatforms()));
        game.setAvailableOnPlan(dto.getAvailableOnPlan());
    }

    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUser().getId());
        dto.setPlanId(subscription.getPlan().getId());
        dto.setPlanName(subscription.getPlan().getName());
        dto.setPlanPrice(subscription.getPlan().getPrice());
        dto.setStatus(subscription.getStatus());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setAutoRenew(subscription.isAutoRenew());
        return dto;
    }

    public static PlanDto toPlanDto(SubscriptionPlan plan) {
        PlanDto dto = new PlanDto();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setPrice(plan.getPrice());
        dto.setMaxDevices(plan.getMaxDevices());
        dto.setDownloadAllowed(plan.isDownloadAllowed());
        dto.setDescription(plan.getDescription());
        return dto;
    }

    public static List<PlanDto> toPlanDtoList(List<SubscriptionPlan> plans) {
        List<PlanDto> result = new ArrayList<>();
        for (SubscriptionPlan plan : plans) {
            result.add(toPlanDto(plan));
        }
        return result;
    }

    public static AdminStatsDto.PopularGameDto toPopularGameDto(Game game) {
        return new AdminStatsDto.PopularGameDto(game.getTitle(), game.getGenre(), game.getRating());
    }
}
