package com.toteuch.animerecommendations.anime.repositories;

import com.toteuch.animerecommendations.anime.AlternativeTitleType;
import com.toteuch.animerecommendations.anime.entities.AlternativeTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlternativeTitleRepository extends JpaRepository<AlternativeTitle, Long> {
    @Query("Select at From AlternativeTitle at Where anime.id = :animeId And type = :type")
    AlternativeTitle findByAnimeIdAndType(Integer animeId, AlternativeTitleType type);

    @Query("Select at From AlternativeTitle at Where anime.id = :animeId And type = com.toteuch.animerecommendations.anime.AlternativeTitleType.SYNONYM")
    List<AlternativeTitle> findSynonymsByAnimeId(Integer animeId);
}
