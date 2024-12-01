package com.toteuch.animerecommendations.anime.repositories;

import com.toteuch.animerecommendations.anime.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
