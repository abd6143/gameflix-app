package com.gameflix.service;

import com.gameflix.dto.GameDto;
import com.gameflix.entity.Game;
import com.gameflix.entity.PlanTier;
import com.gameflix.exception.ResourceNotFoundException;
import com.gameflix.mapper.EntityMapper;
import com.gameflix.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Page<GameDto> findAll(String genre, String platform, PlanTier plan, Pageable pageable) {
        log.info("Entering findAll genre={}, platform={}, plan={}", genre, platform, plan);
        Page<Game> games;

        if (StringUtils.hasText(genre) && plan != null) {
            games = gameRepository.findByGenreAndPlan(genre, plan, pageable);
        } else if (StringUtils.hasText(genre)) {
            games = gameRepository.findByGenreIgnoreCase(genre, pageable);
        } else if (StringUtils.hasText(platform)) {
            games = gameRepository.findByPlatform(platform, pageable);
        } else if (plan != null) {
            games = gameRepository.findByAvailableOnPlan(plan, pageable);
        } else {
            games = gameRepository.findAll(pageable);
        }

        Page<GameDto> result = games.map(EntityMapper::toGameDto);
        log.info("Exiting findAll with {} results", result.getTotalElements());
        return result;
    }

    public GameDto findById(Long id) {
        log.info("Entering findById id={}", id);
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + id));
        GameDto dto = EntityMapper.toGameDto(game);
        log.info("Exiting findById id={}", id);
        return dto;
    }

    public Page<GameDto> search(String query, Pageable pageable) {
        log.info("Entering search query={}", query);
        Page<GameDto> result = gameRepository.searchByTitleOrDeveloper(query, pageable)
                .map(EntityMapper::toGameDto);
        log.info("Exiting search with {} results", result.getTotalElements());
        return result;
    }

    @Transactional
    public GameDto save(GameDto gameDto) {
        log.info("Entering save title={}", gameDto.getTitle());
        Game game = EntityMapper.toGameEntity(gameDto);
        Game saved = gameRepository.save(game);
        GameDto dto = EntityMapper.toGameDto(saved);
        log.info("Exiting save id={}", saved.getId());
        return dto;
    }

    @Transactional
    public GameDto update(Long id, GameDto gameDto) {
        log.info("Entering update id={}", id);
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + id));
        EntityMapper.updateGameFromDto(game, gameDto);
        Game saved = gameRepository.save(game);
        GameDto dto = EntityMapper.toGameDto(saved);
        log.info("Exiting update id={}", id);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        log.info("Entering delete id={}", id);
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game not found with id: " + id);
        }
        gameRepository.deleteById(id);
        log.info("Exiting delete id={}", id);
    }

    public Page<GameDto> findSimilar(String genre, Long excludeId, Pageable pageable) {
        log.info("Entering findSimilar genre={}", genre);
        Page<GameDto> result = gameRepository.findByGenreIgnoreCase(genre, pageable)
                .map(EntityMapper::toGameDto);
        log.info("Exiting findSimilar");
        return result;
    }
}
