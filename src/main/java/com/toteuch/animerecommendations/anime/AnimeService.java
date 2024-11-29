package com.toteuch.animerecommendations.anime;

import com.toteuch.animerecommendations.malapi.AnimeDetailsRaw;
import com.toteuch.animerecommendations.malapi.MalApi;
import com.toteuch.animerecommendations.malapi.exception.MalApiException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnimeService {

    private static final Logger log = LoggerFactory.getLogger(AnimeService.class);

    @Value("${app.anime.refreshLimitInDays}")
    private Integer refreshLimitInDays;

    @Autowired
    private MalApi malApi;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AnimeRepository animeRepository;

    public void refreshAnimeDetails(int animeId) {
        log.debug("Getting details of anime {}", animeId);
        try {
            AnimeDetailsRaw animeDetailsRaw = malApi.getAnimeDetails(animeId);
            List<Genre> genres = new ArrayList<>();
            if (animeDetailsRaw.getGenres() != null) {
                for (Map.Entry<Integer, String> entry : animeDetailsRaw.getGenres().entrySet()) {
                    Genre genre = genreRepository.findById(entry.getKey()).orElse(null);
                    if (genre == null) {
                        genre = genreRepository.save(new Genre(entry.getKey(), entry.getValue()));
                    }
                    genres.add(genre);
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
            log.error("Unknown error {} when getting anime details of anime {} : {}", e.getStatusCode(), animeId, e.getMessage());
        }
    }

    public Anime getAnimeDetailsToRefresh() {
        Anime anime = animeRepository.findTopByDetailsUpdateIsNull();
        if (anime != null) {
            return anime;
        }
        Calendar limitDate = Calendar.getInstance();
        limitDate.add(Calendar.DAY_OF_WEEK, -refreshLimitInDays);
        log.debug("Requesting anime with details older than {}", limitDate.getTime());
        return animeRepository.findTopByDetailsUpdateBeforeOrderByDetailsUpdateAsc(limitDate.getTime());
    }

    public long getAnimeCount() {
        return animeRepository.count();
    }

    public long getAnimeDetailsToGetCount() {
        return animeRepository.countByDetailsUpdateIsNull();
    }
}
