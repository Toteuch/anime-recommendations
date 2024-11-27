package com.toteuch.animerecommendations.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {
    Anime findTopByOrderByDetailsUpdateAsc();

    Anime findTopByDetailsUpdateIsNull();
}
