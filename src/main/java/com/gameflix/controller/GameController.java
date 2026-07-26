package com.gameflix.controller;
import com.gameflix.dto.ApiResponse;
import com.gameflix.dto.GameDto;
import com.gameflix.entity.PlanTier;
import com.gameflix.service.GameService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<GameDto>>> listGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) PlanTier plan,
            @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<GameDto> games = gameService.findAll(genre, platform, plan, pageable);
        return ResponseEntity.ok(ApiResponse.ok(games));
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<GameDto>>> searchGames(
            @RequestParam("q") String query,
            @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<GameDto> games = gameService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.ok(games));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GameDto>> getGame(@PathVariable Long id) {
        GameDto game = gameService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(game));
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GameDto>> createGame(@Valid @RequestBody GameDto gameDto) {
        GameDto created = gameService.save(gameDto);
        return ResponseEntity.ok(ApiResponse.ok(created, "Game created"));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GameDto>> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody GameDto gameDto) {
        GameDto updated = gameService.update(id, gameDto);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Game updated"));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGame(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Game deleted"));
    }
}
