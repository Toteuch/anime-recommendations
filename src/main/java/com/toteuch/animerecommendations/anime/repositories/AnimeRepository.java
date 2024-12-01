package com.toteuch.animerecommendations.anime.repositories;

import com.toteuch.animerecommendations.anime.entities.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {
    Anime findTopByDetailsUpdateIsNull();

    Anime findTopByDetailsUpdateBeforeOrderByDetailsUpdateAsc(Date detailsUpdate);

    long countByDetailsUpdateIsNull();
}
