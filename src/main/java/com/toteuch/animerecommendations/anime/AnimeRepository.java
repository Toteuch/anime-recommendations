package com.toteuch.animerecommendations.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Integer> {
    Anime findTopByDetailsUpdateIsNull();

    Anime findTopByDetailsUpdateBeforeOrderByDetailsUpdateAsc(Date detailsUpdate);

    long countByDetailsUpdateIsNull();
}
