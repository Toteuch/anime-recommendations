package com.toteuch.animerecommendations.malapi;

public class UserAnimeScoreRaw {
    private String username;
    private Integer animeId;
    private String animeTitle;
    private int userScore;

    public UserAnimeScoreRaw(String username, Integer animeId, String animeTitle, int userScore) {
        this.username = username;
        this.animeId = animeId;
        this.animeTitle = animeTitle;
        this.userScore = userScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAnimeId() {
        return animeId;
    }

    public void setAnimeId(Integer animeId) {
        this.animeId = animeId;
    }

    public String getAnimeTitle() {
        return animeTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }
}
