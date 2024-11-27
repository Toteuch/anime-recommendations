package com.toteuch.animerecommendations.useranimescore.task;

import com.toteuch.animerecommendations.useranimescore.UserAnimeScoreService;
import com.toteuch.animerecommendations.userprofile.UserProfile;
import com.toteuch.animerecommendations.userprofile.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

public class RefreshUserAnimeScoresTask {
    private static final Logger log = LoggerFactory.getLogger(RefreshUserAnimeScoresTask.class);

    @Value("${app.userprofile.referenceUser}")
    private String referenceUsername;

    @Autowired
    private UserAnimeScoreService userAnimeScoreService;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Scheduled(fixedDelayString = "${app.task.refreshUserAnimeScoresTask.fixedDelay}")
    private void refreshUserAnimeScores() {

        // initialize with reference
        UserProfile reference = userProfileRepository.findByUsername(referenceUsername);
        userAnimeScoreService.updateUserAnimeScore(reference);
        log.info("Initializing refreshUserAnimeScores task");
        while (true) {
            // get the userprofile that don't have any value in animeList and that is the oldest seen
            UserProfile userProfile = userProfileRepository.findTopByAnimeListSizeOrderByLastSeenAsc(-1);
            if (userProfile == null) {
                // if none, get the oldest updated userprofile
                userProfile = userProfileRepository.findTopByOrderByLastUpdateAsc();
            }
            log.info("Updating {}'s animelist", userProfile.getUsername());
            // update this user
            userAnimeScoreService.updateUserAnimeScore(userProfile);
        }
    }
}
