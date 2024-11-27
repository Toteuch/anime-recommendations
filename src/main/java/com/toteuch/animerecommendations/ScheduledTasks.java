package com.toteuch.animerecommendations;

import com.toteuch.animerecommendations.anime.task.RefreshAnimeDetailsTask;
import com.toteuch.animerecommendations.useranimescore.task.RefreshUserAnimeScoresTask;
import com.toteuch.animerecommendations.userprofile.task.RefreshAffinitiesTask;
import com.toteuch.animerecommendations.userprofile.task.RetrieveAndPurgeUsersTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(4);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    @ConditionalOnProperty(value = "app.task.refreshAnimeDetailsTask.enabled", matchIfMissing = true, havingValue = "true")
    public RefreshAnimeDetailsTask refreshAnimeDetailsTask() {
        return new RefreshAnimeDetailsTask();
    }

    @Bean
    @ConditionalOnProperty(value = "app.task.retrieveAndPurgeUsersTask.enabled", matchIfMissing = true, havingValue = "true")
    public RetrieveAndPurgeUsersTask retrieveAndPurgeUsersTask() {
        return new RetrieveAndPurgeUsersTask();
    }

    @Bean
    @ConditionalOnProperty(value = "app.task.refreshAffinitiesTask.enabled", matchIfMissing = true, havingValue = "true")
    public RefreshAffinitiesTask refreshAffinitiesTask() {
        return new RefreshAffinitiesTask();
    }

    @Bean
    @ConditionalOnProperty(value = "app.task.refreshUserAnimeScoresTask.enabled", matchIfMissing = true, havingValue = "true")
    public RefreshUserAnimeScoresTask refreshUserAnimeScoresTask() {
        return new RefreshUserAnimeScoresTask();
    }

}
