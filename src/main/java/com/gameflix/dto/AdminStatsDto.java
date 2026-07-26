package com.gameflix.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminStatsDto {

    private long totalUsers;
    private long activeSubscriptions;
    private BigDecimal monthlyRevenue;
    private long totalGames;
    private List<PopularGameDto> popularGames = new ArrayList<>();

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public void setActiveSubscriptions(long activeSubscriptions) {
        this.activeSubscriptions = activeSubscriptions;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public long getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(long totalGames) {
        this.totalGames = totalGames;
    }

    public List<PopularGameDto> getPopularGames() {
        return popularGames;
    }

    public void setPopularGames(List<PopularGameDto> popularGames) {
        this.popularGames = popularGames;
    }

    public static class PopularGameDto {
        private String title;
        private String genre;
        private double rating;

        public PopularGameDto() {
        }

        public PopularGameDto(String title, String genre, double rating) {
            this.title = title;
            this.genre = genre;
            this.rating = rating;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }
    }
}
