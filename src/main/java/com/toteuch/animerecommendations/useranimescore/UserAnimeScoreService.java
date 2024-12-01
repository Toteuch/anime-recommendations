package com.toteuch.animerecommendations.useranimescore;

import com.toteuch.animerecommendations.anime.entities.Anime;
import com.toteuch.animerecommendations.anime.repositories.AnimeRepository;
import com.toteuch.animerecommendations.malapi.MalApi;
import com.toteuch.animerecommendations.malapi.UserAnimeScoreRaw;
import com.toteuch.animerecommendations.malapi.exception.MalApiException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListNotFoundException;
import com.toteuch.animerecommendations.malapi.exception.MalApiListVisibilityException;
import com.toteuch.animerecommendations.userprofile.UserProfile;
import com.toteuch.animerecommendations.userprofile.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserAnimeScoreService {

    private static final Logger log = LoggerFactory.getLogger(UserAnimeScoreService.class);

    @Autowired
    private MalApi malApi;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private UserAnimeScoreRepository userAnimeScoreRepository;

    public void updateUserAnimeScore(UserProfile userProfile) {
        try {
            List<UserAnimeScoreRaw> userAnimeScoreRawList = malApi.getUserAnimeListScore(userProfile.getUsername());
            int userAnimeCount = 0;
            int userRatedAnimeCount = 0;
            for (UserAnimeScoreRaw i : userAnimeScoreRawList) {
                userAnimeCount++;
                if (i.getUserScore() > 0) {
                    userRatedAnimeCount++;
                    Anime anime = animeRepository.findById(i.getAnimeId()).orElse(null);
                    if (anime == null) {
                        anime = new Anime(i.getAnimeId(), i.getAnimeTitle());
                        anime = animeRepository.save(anime);
                    }
                    UserAnimeScore userAnimeScore = userAnimeScoreRepository.findByUserProfileAndAnime(userProfile, anime);
                    if (userAnimeScore == null) {
                        userAnimeScore = new UserAnimeScore(userProfile, anime);
                    }
                    userAnimeScore.setScore(i.getUserScore());
                    userAnimeScoreRepository.save(userAnimeScore);
                }
            }
            userProfile.setAnimeListSize(userAnimeCount);
            userProfile.setAnimeRatedCount(userRatedAnimeCount);
            userProfile.setLastUpdate(new Date());
            userProfileRepository.save(userProfile);
            log.info("User {} updated : animeListSize {} | animeRatedCount {}",
                    userProfile.getUsername(), userProfile.getAnimeListSize(), userProfile.getAnimeRatedCount());
        } catch (MalApiListVisibilityException e) {
            log.info("User {} has set its list visibility to private", userProfile.getUsername());
            userProfile.setAnimeRatedCount(0);
            userProfile.setAnimeListSize(0);
            userProfile.setLastUpdate(new Date());
            userProfileRepository.save(userProfile);
        } catch (MalApiListNotFoundException e) {
            log.info("Deleting user {}, not found", userProfile.getUsername());
            userProfileRepository.delete(userProfile);
        } catch (MalApiException e) {
            log.error("Error while requesting animeList of user {} : {} - {}",
                    userProfile.getUsername(), e.getStatusCode(), e.getMessage());
        }
    }
}
