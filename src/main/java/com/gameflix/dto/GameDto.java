package com.gameflix.dto;

import com.gameflix.entity.PlanTier;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class GameDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Developer is required")
    private String developer;

    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Min(1970)
    @Max(2100)
    private int releaseYear;

    @Min(0)
    @Max(10)
    private double rating;

    private String coverImageUrl;

    private List<String> platforms = new ArrayList<>();

    @NotNull(message = "Plan tier is required")
    private PlanTier availableOnPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public PlanTier getAvailableOnPlan() {
        return availableOnPlan;
    }

    public void setAvailableOnPlan(PlanTier availableOnPlan) {
        this.availableOnPlan = availableOnPlan;
    }
}
