package com.toteuch.animerecommendations.anime.repositories;

import com.toteuch.animerecommendations.anime.entities.Anime;
import com.toteuch.animerecommendations.anime.entities.PictureLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureLinkRepository extends JpaRepository<PictureLink, Long> {
    List<PictureLink> findByAnime(Anime anime);
}
