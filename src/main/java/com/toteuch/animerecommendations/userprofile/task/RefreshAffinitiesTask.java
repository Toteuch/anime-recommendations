package com.toteuch.animerecommendations.userprofile.task;

import com.toteuch.animerecommendations.userprofile.UserProfile;
import com.toteuch.animerecommendations.userprofile.UserProfileRepository;
import com.toteuch.animerecommendations.userprofile.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

public class RefreshAffinitiesTask {
    private static final Logger log = LoggerFactory.getLogger(RefreshAffinitiesTask.class);

    @Value("${app.task.refreshAffinitiesTask.sleepInMinute}")
    private Integer sleepInMinute;

    @Autowired
    private UserProfileService service;
    @Autowired
    private UserProfileRepository repo;

    @Scheduled(fixedDelayString = "${app.task.refreshAffinitiesTask.fixedDelay}")
    public void refreshAffinities() {
        log.info("Initializing refreshAffinities task");
        while (true) {
            UserProfile userProfile = repo.findTopByAnimeRatedCountGreaterThanAndAffinityIsNullOrderByLastUpdateAsc(0);
            if (userProfile == null) {
                userProfile = repo.findAffinityToUpdate();
            }
            if (userProfile != null) {
                log.info("Calculating affinity with user {} ...", userProfile.getUsername());
                service.calculateAffinity(userProfile);
            } else {
                log.debug("All affinities has been calculated...");
                try {
                    Thread.sleep(sleepInMinute * 60 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
