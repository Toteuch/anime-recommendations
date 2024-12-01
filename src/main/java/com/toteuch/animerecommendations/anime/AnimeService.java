package com.toteuch.animerecommendations.anime;

import com.toteuch.animerecommendations.anime.entities.*;
import com.toteuch.animerecommendations.anime.repositories.*;
import com.toteuch.animerecommendations.malapi.AnimeDetailsRaw;
import com.toteuch.animerecommendations.malapi.MalApi;
import com.toteuch.animerecommendations.malapi.exception.MalApiException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private AlternativeTitleRepository alternativeTitleRepository;
    @Autowired
    private PictureLinkRepository pictureLinkRepository;

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
            Season season = null;
            if (animeDetailsRaw.getStartSeasonYear() != null && animeDetailsRaw.getStartSeasonSeason() != null) {
                season = seasonRepository.findByYearAndSeason(animeDetailsRaw.getStartSeasonYear(), animeDetailsRaw.getStartSeasonSeason());
                if (season == null) {
                    season = new Season(animeDetailsRaw.getStartSeasonYear(), animeDetailsRaw.getStartSeasonSeason());
                    season = seasonRepository.save(season);
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
                anime.setSeason(season);

                List<AlternativeTitle> alternativeTitles = null;
                if (animeDetailsRaw.getAlternativeTitles() != null) {
                    alternativeTitles = new ArrayList<>();
                    AlternativeTitle atEn = alternativeTitleRepository.findByAnimeIdAndType(animeId, AlternativeTitleType.EN);
                    if (atEn == null) {
                        atEn = new AlternativeTitle(anime, AlternativeTitleType.EN, (String) animeDetailsRaw.getAlternativeTitles().get(AlternativeTitleType.EN.name()));
                        alternativeTitleRepository.save(atEn);
                    }
                    alternativeTitles.add(atEn);
                    AlternativeTitle atJa = alternativeTitleRepository.findByAnimeIdAndType(animeId, AlternativeTitleType.JA);
                    if (atJa == null) {
                        atJa = new AlternativeTitle(anime, AlternativeTitleType.JA, (String) animeDetailsRaw.getAlternativeTitles().get(AlternativeTitleType.JA.name()));
                        alternativeTitleRepository.save(atJa);
                    }
                    alternativeTitles.add(atJa);
                    List<AlternativeTitle> synonyms = alternativeTitleRepository.findSynonymsByAnimeId(animeId);
                    alternativeTitleRepository.deleteAll(synonyms);
                    synonyms = new ArrayList<>();
                    if (animeDetailsRaw.getAlternativeTitles().get(AlternativeTitleType.SYNONYM.name()) != null) {
                        String[] synonymTexts = (String[]) animeDetailsRaw.getAlternativeTitles().get(AlternativeTitleType.SYNONYM.name());
                        for (String synonymText : synonymTexts) {
                            AlternativeTitle atSynonym = new AlternativeTitle(anime, AlternativeTitleType.SYNONYM, synonymText);
                            atSynonym = alternativeTitleRepository.save(atSynonym);
                            synonyms.add(atSynonym);
                        }
                    }
                    alternativeTitles.addAll(synonyms);
                }
                anime.setAlternativeTitles(alternativeTitles);
                List<PictureLink> pictureLinks = pictureLinkRepository.findByAnime(anime);
                pictureLinkRepository.deleteAll(pictureLinks);
                pictureLinks = new ArrayList<>();
                if (animeDetailsRaw.getPictureUrlsMedium() != null) {
                    for (String pictureUrlMedium : animeDetailsRaw.getPictureUrlsMedium()) {
                        PictureLink pictureLink = new PictureLink(pictureUrlMedium, anime);
                        pictureLink = pictureLinkRepository.save(pictureLink);
                        pictureLinks.add(pictureLink);
                    }
                }
                anime.setPictureLinks(pictureLinks);
                anime.setMainPictureMediumUrl(animeDetailsRaw.getMainPictureUrlMedium());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                anime.setStartDate(animeDetailsRaw.getStartDate() != null ? sdf.parse(animeDetailsRaw.getStartDate()) : null);
                anime.setEndDate(animeDetailsRaw.getEndDate() != null ? sdf.parse(animeDetailsRaw.getEndDate()) : null);
                anime.setSource(animeDetailsRaw.getSource());
                anime.setRating(animeDetailsRaw.getRating());
                anime.setStatus(animeDetailsRaw.getStatus());
                anime = animeRepository.save(anime);
                log.info("Anime details {}({}) updated", anime.getTitle(), anime.getId());
            } else {
                log.error("Anime {} not found in DB", animeId);
            }
        } catch (MalApiListNotFoundException e) {
            log.warn("Anime {} is not found in MAL", animeId);
        } catch (MalApiException e) {
            log.error("Unknown error {} when getting anime details of anime {} : {}", e.getStatusCode(), animeId, e.getMessage());
        } catch (ParseException e) {
            log.error("Couldn't parse dates from MAL API : {}", e.getMessage());
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
