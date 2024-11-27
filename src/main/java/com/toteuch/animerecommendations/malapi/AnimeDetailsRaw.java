package com.toteuch.animerecommendations.malapi;

import java.util.Map;

public class AnimeDetailsRaw {
    private Integer id;
    private String title;
    private String mediaType;
    private Map<Integer, String> genres;
    private String status;
    private Double score;
    private Integer numEpisodes;
    private Integer prequelAnimeId;
    private Integer sequelAnimeId;

    public AnimeDetailsRaw() {
    }

    public AnimeDetailsRaw(Integer id, String title, String mediaType, Map<Integer, String> genres,
                           String status, Double score, Integer numEpisodes) {
        this.id = id;
        this.title = title;
        this.mediaType = mediaType;
        this.genres = genres;
        this.status = status;
        this.score = score;
        this.numEpisodes = numEpisodes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Map<Integer, String> getGenres() {
        return genres;
    }

    public void setGenres(Map<Integer, String> genres) {
        this.genres = genres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(Integer numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public Integer getPrequelAnimeId() {
        return prequelAnimeId;
    }

    public void setPrequelAnimeId(Integer prequelAnimeId) {
        this.prequelAnimeId = prequelAnimeId;
    }

    public Integer getSequelAnimeId() {
        return sequelAnimeId;
    }

    public void setSequelAnimeId(Integer sequelAnimeId) {
        this.sequelAnimeId = sequelAnimeId;
    }
}
