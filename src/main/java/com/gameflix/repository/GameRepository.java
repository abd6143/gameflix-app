package com.gameflix.repository;

import com.gameflix.entity.Game;
import com.gameflix.entity.PlanTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Page<Game> findByGenreIgnoreCase(String genre, Pageable pageable);

    Page<Game> findByAvailableOnPlan(PlanTier planTier, Pageable pageable);

    @Query("SELECT g FROM Game g WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :query, '%')) "
            + "OR LOWER(g.developer) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Game> searchByTitleOrDeveloper(@Param("query") String query, Pageable pageable);

    @Query("SELECT g FROM Game g JOIN g.platforms p WHERE LOWER(p) = LOWER(:platform)")
    Page<Game> findByPlatform(@Param("platform") String platform, Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.genre = :genre AND g.availableOnPlan = :plan")
    Page<Game> findByGenreAndPlan(@Param("genre") String genre, @Param("plan") PlanTier plan, Pageable pageable);

    long countByGenre(String genre);
}
