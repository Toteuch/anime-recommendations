package com.toteuch.animerecommendations.anime;

import com.toteuch.animerecommendations.malapi.AnimeDetailsRaw;
import com.toteuch.animerecommendations.malapi.MalApi;
import com.toteuch.animerecommendations.malapi.exception.MalApiException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnimeService {

    private static final Logger log = LoggerFactory.getLogger(AnimeService.class);

    @Autowired
    private MalApi malApi;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AnimeRepository animeRepository;

    public void getAnimeDetails(int animeId) {
        log.debug("Getting details of anime {}", animeId);
        try {
            AnimeDetailsRaw animeDetailsRaw = malApi.getAnimeDetails(animeId);
            List<Genre> genres = new ArrayList<>();
            if (animeDetailsRaw.getGenres() != null) {
                for (Map.Entry<Integer, String> entry : animeDetailsRaw.getGenres().entrySet()) {
                    Genre genre = genreRepository.findById(entry.getKey()).orElse(null);
                    if (genre == null) {
                        genre = new Genre(entry.getKey(), entry.getValue());
                        genres.add(genreRepository.save(genre));
                    }
                }
            }
            Anime anime = animeRepository.findById(animeId).orElse(null);
            if (anime != null) {
                anime.setGenres(genres);
                anime.setAnimeScore(animeDetailsRaw.getScore());
                anime.setTitle(animeDetailsRaw.getTitle());
                anime.setMediaType(animeDetailsRaw.getMediaType());
                anime.setNumEpisodes(animeDetailsRaw.getNumEpisodes());
                anime.setPrequelAnimeId(animeDetailsRaw.getPrequelAnimeId());
                anime.setSequelAnimeId(animeDetailsRaw.getSequelAnimeId());
                anime.setDetailsUpdate(new Date());
                anime = animeRepository.save(anime);
                log.info("Anime details {}({}) updated", anime.getTitle(), anime.getId());
            } else {
                log.error("Anime {} not found in DB", animeId);
            }
        } catch (MalApiListNotFoundException e) {
            log.warn("Anime {} is not found in MAL", animeId);
        } catch (MalApiException e) {
            log.error("Unknow error {} when getting anime details of anime {} : {}", e.getStatusCode(), animeId, e.getMessage());
        }
    }

    public Anime getAnimeDetailsToRefresh() {
        Anime anime = animeRepository.findTopByDetailsUpdateIsNull();
        if (anime != null) {
            return anime;
        }
        anime = animeRepository.findTopByOrderByDetailsUpdateAsc();
        Calendar yesterdaySameTime = Calendar.getInstance();
        yesterdaySameTime.add(Calendar.HOUR, -24);

        if (anime != null && anime.getDetailsUpdate() != null && anime.getDetailsUpdate().before(yesterdaySameTime.getTime())) {
            return anime;
        } else {
            return null;
        }
    }

    private void testGetAnimeDetails() {
        getAnimeDetails(52034);
        getAnimeDetails(55791);
    }
}
