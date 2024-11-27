package com.toteuch.animerecommendations.anime.task;

import com.toteuch.animerecommendations.anime.Anime;
import com.toteuch.animerecommendations.anime.AnimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

public class RefreshAnimeDetailsTask {
    private static final Logger log = LoggerFactory.getLogger(RefreshAnimeDetailsTask.class);

    @Value("${app.task.refreshAnimeDetailsTask.sleepInMinute}")
    private int sleepInMinute;

    @Autowired
    private AnimeService animeService;

    @Scheduled(fixedDelayString = "${app.task.refreshAnimeDetailsTask.fixedDelay}")
    private void refreshAnimeDetails() throws InterruptedException {
        log.info("Initializing refreshAnimeDetails task");
        while (true) {
            Anime anime = animeService.getAnimeDetailsToRefresh();
            if (anime != null) {
                animeService.getAnimeDetails(anime.getId());
            } else {
                log.debug("No anime details to refresh");
                Thread.sleep((long) sleepInMinute * 60 * 1000);
            }
        }
    }
}
