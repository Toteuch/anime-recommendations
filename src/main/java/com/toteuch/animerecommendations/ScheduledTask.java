package com.toteuch.animerecommendations;

import com.toteuch.animerecommendations.anime.Anime;
import com.toteuch.animerecommendations.anime.AnimeService;
import com.toteuch.animerecommendations.useranimescore.UserAnimeScoreService;
import com.toteuch.animerecommendations.userprofile.UserProfile;
import com.toteuch.animerecommendations.userprofile.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    private static boolean isInitialized = false;
    @Value("${app.task.refreshAnimeDetailsTask.enabled}")
    private boolean isRefreshAnimeDetailsTaskEnabled;
    @Value("${app.task.refreshUserAnimeScoresTask.enabled}")
    private boolean isRefreshUserAnimeScoresTaskEnabled;
    @Value("${app.task.computeUsersAffinityTask.enabled}")
    private boolean isComputeUsersAffinityTaskEnabled;
    @Value("${app.task.purgeUsersTask.enabled}")
    private boolean isPurgeUsersTaskEnabled;
    @Value("${app.task.retrieveUsersTask.enabled}")
    private boolean isRetrieveUsersTaskEnabled;
    @Autowired
    private AnimeService animeService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserAnimeScoreService userAnimeScoreService;

    private void initializeScheduledTask() {
        if (!isInitialized) {
            isInitialized = true;
            log.info("Initializing scheduled tasks");
            log.info("refreshUserAnimeScoresTask {} enabled", isRefreshUserAnimeScoresTaskEnabled ? "is" : "is not");
            log.info("refreshAnimeDetailsTask {} enabled", isRefreshAnimeDetailsTaskEnabled ? "is" : "is not");
            log.info("computeUsersAffinityTask {} enabled", isComputeUsersAffinityTaskEnabled ? "is" : "is not");
            log.info("purgeUsersTaskEnabled {} enabled", isPurgeUsersTaskEnabled ? "is" : "is not");
            log.info("retrieveUsersTaskEnabled {} enabled", isRetrieveUsersTaskEnabled ? "is" : "is not");
        }
    }

    @Scheduled(fixedDelay = 100)
    public void scheduledTask() {
        initializeScheduledTask();
        if (isRefreshUserAnimeScoresTaskEnabled) {
            UserProfile userProfile = userProfileService.getUserAnimeScoreUserToRefresh();
            while (userProfile != null) {
                userAnimeScoreService.updateUserAnimeScore(userProfile);
                userProfile = userProfileService.getUserAnimeScoreUserToRefresh();
            }
            log.debug("No user anime list to refresh");
        }
        if (isRefreshAnimeDetailsTaskEnabled) {
            Anime anime = animeService.getAnimeDetailsToRefresh();
            while (anime != null) {
                animeService.refreshAnimeDetails(anime.getId());
                anime = animeService.getAnimeDetailsToRefresh();
            }
            log.debug("No anime details to refresh");
        }
        if (isComputeUsersAffinityTaskEnabled) {
            UserProfile userProfile = userProfileService.getUserAffinityToCompute();
            while (userProfile != null) {
                userProfileService.computeAffinity(userProfile);
                userProfile = userProfileService.getUserAffinityToCompute();
            }
            log.debug("No affinities to compute");
        }
        if (isPurgeUsersTaskEnabled) {
            userProfileService.purgeUsers();
        }
        if (isRetrieveUsersTaskEnabled) {
            userProfileService.retrieveUsers();
        }
    }
}
