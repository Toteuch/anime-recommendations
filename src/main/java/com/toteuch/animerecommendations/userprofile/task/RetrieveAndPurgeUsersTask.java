package com.toteuch.animerecommendations.userprofile.task;

import com.toteuch.animerecommendations.userprofile.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class RetrieveAndPurgeUsersTask {

    private static final Logger log = LoggerFactory.getLogger(RetrieveAndPurgeUsersTask.class);

    @Autowired
    private UserProfileService service;

    @Scheduled(fixedDelayString = "${app.task.retrieveAndPurgeUsersTask.fixedDelay}")
    public void retrieveAndPurgeUsersTask() {
        log.debug("Start task retrieveAndPurgeUsersTask");
        service.retrieveAndPurgeUserProfile();
        log.debug("End task retrieveAndPurgeUsersTask");
    }
}
