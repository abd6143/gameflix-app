package com.gameflix;

import com.gameflix.dto.GameDto;
import com.gameflix.entity.Game;
import com.gameflix.entity.PlanTier;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.repository.GameRepository;
import com.gameflix.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void findAll_returnsPaginatedList() {
        Game game = createSampleGame();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Game> page = new PageImpl<>(List.of(game), pageable, 1);
        when(gameRepository.findAll(pageable)).thenReturn(page);

        Page<GameDto> result = gameService.findAll(null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Cyber Nexus", result.getContent().get(0).getTitle());
    }

    @Test
    void findById_throwsForMissingId() {
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findById(999L));
    }

    @Test
    void save_persistsAndReturnsDto() {
        GameDto dto = new GameDto();
        dto.setTitle("New Game");
        dto.setGenre("Action");
        dto.setDescription("A new game");
        dto.setDeveloper("Dev Co");
        dto.setPublisher("Pub Co");
        dto.setReleaseYear(2025);
        dto.setRating(8.5);
        dto.setPlatforms(Arrays.asList("PC"));
        dto.setAvailableOnPlan(PlanTier.BASIC);

        Game saved = createSampleGame();
        saved.setTitle("New Game");
        when(gameRepository.save(any(Game.class))).thenReturn(saved);

        GameDto result = gameService.save(dto);

        assertNotNull(result);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void search_filtersCorrectly() {
        Game game = createSampleGame();
        Pageable pageable = PageRequest.of(0, 20);
        when(gameRepository.searchByTitleOrDeveloper(eq("Cyber"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(game)));

        Page<GameDto> result = gameService.search("Cyber", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Cyber Nexus", result.getContent().get(0).getTitle());
    }

    private Game createSampleGame() {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Cyber Nexus");
        game.setGenre("Action");
        game.setDescription("Neon action RPG");
        game.setDeveloper("Neon Forge");
        game.setPublisher("GameFlix");
        game.setReleaseYear(2024);
        game.setRating(9.2);
        game.setPlatforms(Arrays.asList("PC", "PlayStation"));
        game.setAvailableOnPlan(PlanTier.PREMIUM);
        return game;
    }
}
