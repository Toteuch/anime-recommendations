package com.toteuch.animerecommendations.api;

import com.toteuch.animerecommendations.anime.AnimeService;
import com.toteuch.animerecommendations.userprofile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AnimeRecommendationsController {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private AnimeService animeService;

    @GetMapping("db-stats")
    public String getDbStats() {
        long userCount = userProfileService.getUserCount();
        long userAnimeScoreToGetCount = userProfileService.getUserAnimeScoreToGetCount();
        Date oldestAnimeListDate = userProfileService.getOldestUserUpdate();
        long animeCount = animeService.getAnimeCount();
        long animeDetailsToGetCount = animeService.getAnimeDetailsToGetCount();
        Date newestUserLastSeenDate = userProfileService.getNewestLastSeenUser();
        return String.format("Number of user: %s\n" +
                        "Number of animeList to get: %s\n" +
                        "Oldest animeList: %s\n" +
                        "Number of anime: %s\n" +
                        "Number of animeDetails to get: %s\n" +
                        "Newest user's lastSeen date: %s",
                userCount, userAnimeScoreToGetCount, oldestAnimeListDate,
                animeCount, animeDetailsToGetCount, newestUserLastSeenDate);
    }

    @GetMapping("least-eligible-affinity")
    public Double getLeastEligibleAffinity() {
        return userProfileService.getLeastEligibleAffinity();
    }

}
