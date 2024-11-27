package com.toteuch.animerecommendations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnimeRecommendationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimeRecommendationsApplication.class, args);
    }

}
