package com.gameflix.service;

import com.gameflix.dto.GameDto;
import com.gameflix.entity.PlanTier;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.repository.GameRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GameServiceIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    private GameDto cyberNexus;
    private GameDto pixelQuest;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();

        cyberNexus = gameService.save(buildGame(
                "Cyber Nexus",
                "Action",
                "A neon-soaked open-world action RPG.",
                "Neon Forge Studios",
                "GameFlix Originals",
                2024,
                9.2,
                PlanTier.PREMIUM,
                List.of("PC", "PlayStation")));

        pixelQuest = gameService.save(buildGame(
                "Pixel Quest",
                "RPG",
                "A charming retro-inspired RPG.",
                "Indie Pixel Co",
                "IndieFlix",
                2021,
                7.8,
                PlanTier.BASIC,
                List.of("PC", "Switch")));
    }

    @Test
    void findAll_returnsAllGames() {
        Pageable pageable = PageRequest.of(0, 20);

        Page<GameDto> result = gameService.findAll(null, null, null, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().anyMatch(g -> "Cyber Nexus".equals(g.getTitle())));
        assertTrue(result.getContent().stream().anyMatch(g -> "Pixel Quest".equals(g.getTitle())));
    }

    @Test
    void findById_returnsMatchingGame() {
        GameDto found = gameService.findById(cyberNexus.getId());

        assertNotNull(found);
        assertEquals(cyberNexus.getId(), found.getId());
        assertEquals("Cyber Nexus", found.getTitle());
        assertEquals("Action", found.getGenre());
        assertEquals(PlanTier.PREMIUM, found.getAvailableOnPlan());
    }

    @Test
    void search_byTitle_returnsMatchingGames() {
        Pageable pageable = PageRequest.of(0, 20);

        Page<GameDto> result = gameService.search("Cyber", pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Cyber Nexus", result.getContent().get(0).getTitle());
    }

    @Test
    void findById_whenMissing_throwsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.findById(999_999L));
    }

    private GameDto buildGame(
            String title,
            String genre,
            String description,
            String developer,
            String publisher,
            int releaseYear,
            double rating,
            PlanTier plan,
            List<String> platforms) {
        GameDto dto = new GameDto();
        dto.setTitle(title);
        dto.setGenre(genre);
        dto.setDescription(description);
        dto.setDeveloper(developer);
        dto.setPublisher(publisher);
        dto.setReleaseYear(releaseYear);
        dto.setRating(rating);
        dto.setAvailableOnPlan(plan);
        dto.setPlatforms(platforms);
        dto.setCoverImageUrl("https://example.com/" + title.toLowerCase().replace(' ', '-') + ".jpg");
        return dto;
    }
}
