package com.toteuch.animerecommendations.malapi;

import com.toteuch.animerecommendations.anime.AlternativeTitleType;
import com.toteuch.animerecommendations.malapi.exception.MalApiException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListNotFoundException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListVisibilityException;
import com.toteuch.animerecommendations.malapi.response.animedetails.AnimeDetailsResponse;
import com.toteuch.animerecommendations.malapi.response.animedetails.GenreResponse;
import com.toteuch.animerecommendations.malapi.response.animedetails.PictureResponse;
import com.toteuch.animerecommendations.malapi.response.animedetails.RelatedAnimeResponse;
import com.toteuch.animerecommendations.malapi.response.animelistuser.AnimelistUserResponse;
import com.toteuch.animerecommendations.malapi.response.animelistuser.Data;
import com.toteuch.animerecommendations.userprofile.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MalApi {

    private static final Logger log = LoggerFactory.getLogger(MalApi.class);

    private static final int ANIME_LIST_LIMIT = 500;

    private static Long lastCall;

    private static WebClient webClient;
    @Value("${app.malapi.host}")
    private String host;
    @Value("${app.malapi.auth.headerKey}")
    private String authHeaderKey;
    @Value("${app.malapi.auth.clientId}")
    private String authHeaderValue;
    @Value("${app.anime.detailFields}")
    private String animeDetailFields;
    @Autowired
    private UserProfileRepository userProfileRepo;

    private static boolean isSleepNeeded() {
        if (lastCall != null && System.currentTimeMillis() - lastCall < 1000) {
            return true;
        }
        lastCall = System.currentTimeMillis();
        return false;
    }

    private Mono<AnimeDetailsResponse> requestMalPublicApiGetAnimeDetails(int animeId) throws WebClientResponseException {
        WebClient client = getWebClient();
        while (isSleepNeeded()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/anime/{animeId}")
                        .queryParam("fields", "{fields}")
                        .build(animeId, animeDetailFields))
                .retrieve()
                .bodyToMono(AnimeDetailsResponse.class);
    }

    private Mono<AnimelistUserResponse> requestMalPublicApiGetUserAnimeList(String username, int page) throws WebClientResponseException {
        WebClient client = getWebClient();
        while (isSleepNeeded()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{username}/animelist")
                        .queryParam("offset", "{offset}")
                        .queryParam("limit", "{limit}")
                        .queryParam("fields", "{fields}")
                        .build(username, (page - 1) * ANIME_LIST_LIMIT, ANIME_LIST_LIMIT, "list_status"))
                .retrieve()
                .bodyToMono(AnimelistUserResponse.class);
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = WebClient.builder()
                    .baseUrl(host)
                    .defaultHeader(authHeaderKey, authHeaderValue)
                    .build();
        }
        return webClient;
    }

    public AnimeDetailsRaw getAnimeDetails(int animeId) throws MalApiException {
        log.debug("Requesting anime details for anime id {}", animeId);
        AnimeDetailsRaw animeDetailsRaw = null;
        try {
            Mono<AnimeDetailsResponse> response = requestMalPublicApiGetAnimeDetails(animeId);
            AnimeDetailsResponse animeDetailsResponse = response.block();
            if (animeDetailsResponse == null) {
                throw new MalApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Anime details response is empty");
            }
            return parseAnimeDetailsResponse(animeDetailsResponse);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new MalApiListNotFoundException(HttpStatus.NOT_FOUND, "Anime not found");
            } else {
                throw new MalApiException(e.getStatusCode(), "Unknown error");
            }
        }

    }

    private AnimeDetailsRaw parseAnimeDetailsResponse(AnimeDetailsResponse animeDetailsResponse) {
        AnimeDetailsRaw animeDetailsRaw = new AnimeDetailsRaw();
        animeDetailsRaw.setId(animeDetailsResponse.getId());
        animeDetailsRaw.setMediaType(animeDetailsResponse.getMediaType());
        animeDetailsRaw.setScore(animeDetailsResponse.getMean());
        animeDetailsRaw.setTitle(animeDetailsResponse.getTitle());
        animeDetailsRaw.setStatus(animeDetailsResponse.getStatus());
        if (animeDetailsResponse.getGenres() != null) {
            Map<Integer, String> genres = new HashMap<>();
            for (GenreResponse genreResponse : animeDetailsResponse.getGenres()) {
                genres.put(genreResponse.getId(), genreResponse.getName());
            }
            animeDetailsRaw.setGenres(genres);
        }
        animeDetailsRaw.setNumEpisodes(animeDetailsResponse.getNumEpisodes());
        if (null != animeDetailsResponse.getRelatedAnimes()) {
            for (RelatedAnimeResponse relatedAnimeResponse : animeDetailsResponse.getRelatedAnimes()) {
                if (relatedAnimeResponse.getRelationType().equals("sequel")) {
                    animeDetailsRaw.setSequelAnimeId(relatedAnimeResponse.getNode().getId());
                } else if (relatedAnimeResponse.getRelationType().equals("prequel")) {
                    animeDetailsRaw.setPrequelAnimeId(relatedAnimeResponse.getNode().getId());
                }
            }
        }
        if (animeDetailsResponse.getMainPicture() != null) {
            animeDetailsRaw.setMainPictureUrlMedium(animeDetailsResponse.getMainPicture().getMedium());
        }
        if (animeDetailsResponse.getAlternativeTitles() != null) {
            Map<String, Object> alternativeTitles = new HashMap<>();
            alternativeTitles.put(AlternativeTitleType.EN.name(), animeDetailsResponse.getAlternativeTitles().getEn());
            alternativeTitles.put(AlternativeTitleType.JA.name(), animeDetailsResponse.getAlternativeTitles().getJa());
            alternativeTitles.put(
                    AlternativeTitleType.SYNONYM.name(), animeDetailsResponse.getAlternativeTitles().getSynonyms());
            animeDetailsRaw.setAlternativeTitles(alternativeTitles);
        }
        animeDetailsRaw.setStartDate(animeDetailsResponse.getStartDate());
        animeDetailsRaw.setEndDate(animeDetailsResponse.getEndDate());
        if (animeDetailsResponse.getStartSeason() != null) {
            animeDetailsRaw.setStartSeasonYear(animeDetailsResponse.getStartSeason().getYear());
            animeDetailsRaw.setStartSeasonSeason(animeDetailsResponse.getStartSeason().getSeason());
        }
        animeDetailsRaw.setSource(animeDetailsResponse.getSource());
        animeDetailsRaw.setRating(animeDetailsResponse.getRating());
        if (animeDetailsResponse.getPictures() != null) {
            List<String> pictureUrlsMedium = new ArrayList<>();
            for (PictureResponse picture : animeDetailsResponse.getPictures()) {
                pictureUrlsMedium.add(picture.getMedium());
            }
            animeDetailsRaw.setPictureUrlsMedium(pictureUrlsMedium);
        }
        return animeDetailsRaw;
    }

    public List<UserAnimeScoreRaw> getUserAnimeListScore(String username) throws MalApiException {
        List<UserAnimeScoreRaw> retVal = new ArrayList<>();
        int page = 1;
        try {
            log.debug("Requesting animelist of user {}, page {}", username, page);
            Mono<AnimelistUserResponse> response = requestMalPublicApiGetUserAnimeList(username, page);
            AnimelistUserResponse animelistUserResponse = response.block();
            if (animelistUserResponse != null) {
                retVal.addAll(getUserAnimeScoreList(animelistUserResponse, username));
                while (animelistUserResponse != null && animelistUserResponse.getPaging().getNext() != null) {
                    page++;
                    log.debug("Requesting animelist of user {}, page {}", username, page);
                    animelistUserResponse = requestMalPublicApiGetUserAnimeList(username, page).block();
                    if (animelistUserResponse != null) {
                        retVal.addAll(getUserAnimeScoreList(animelistUserResponse, username));
                    } else {
                        log.error("AnimeListUserResponse is null for the username {}, page {}", username, page);
                    }
                }
            } else {
                log.error("AnimeListUserResponse is null for the username {}, page {}", username, page);
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new MalApiListVisibilityException(HttpStatus.FORBIDDEN, "User restricted its list visibility");
            } else if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new MalApiListNotFoundException(HttpStatus.NOT_FOUND, "User not found");
            } else {
                throw new MalApiException(e.getStatusCode(), "Unknown error");
            }
        }
        log.debug("User {} got {} entries in its animelist", username, retVal.size());
        return retVal;
    }

    private List<UserAnimeScoreRaw> getUserAnimeScoreList(AnimelistUserResponse animelistUserResponse, String username) {
        List<UserAnimeScoreRaw> retVal = new ArrayList<>();
        for (Data data : animelistUserResponse.getData()) {
            retVal.add(new UserAnimeScoreRaw(
                    username,
                    data.getNode().getId(),
                    data.getNode().getTitle(),
                    data.getListStatus().getScore()
            ));
        }
        return retVal;
    }
}
