package com.toteuch.animerecommendations.anime.repositories;

import com.toteuch.animerecommendations.anime.entities.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Season findByYearAndSeason(Integer year, String season);
}
