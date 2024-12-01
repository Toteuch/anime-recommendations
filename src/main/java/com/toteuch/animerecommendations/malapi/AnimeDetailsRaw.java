package com.toteuch.animerecommendations.malapi;

import java.util.List;
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
    private String mainPictureUrlMedium;
    private Map<String, Object> alternativeTitles;
    private String startDate;
    private String endDate;
    private Integer startSeasonYear;
    private String startSeasonSeason;
    private String source;
    private String rating;
    private List<String> pictureUrlsMedium;

    public AnimeDetailsRaw() {
    }

    public AnimeDetailsRaw(Integer id, String title, String mediaType, Map<Integer, String> genres, String status, Double score, Integer numEpisodes, Integer prequelAnimeId, Integer sequelAnimeId, String mainPictureUrlMedium, Map<String, Object> alternativeTitles, String startDate, String endDate, Integer startSeasonYear, String startSeasonSeason, String source, String rating, List<String> pictureUrlsMedium) {
        this.id = id;
        this.title = title;
        this.mediaType = mediaType;
        this.genres = genres;
        this.status = status;
        this.score = score;
        this.numEpisodes = numEpisodes;
        this.prequelAnimeId = prequelAnimeId;
        this.sequelAnimeId = sequelAnimeId;
        this.mainPictureUrlMedium = mainPictureUrlMedium;
        this.alternativeTitles = alternativeTitles;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startSeasonYear = startSeasonYear;
        this.startSeasonSeason = startSeasonSeason;
        this.source = source;
        this.rating = rating;
        this.pictureUrlsMedium = pictureUrlsMedium;
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

    public String getMainPictureUrlMedium() {
        return mainPictureUrlMedium;
    }

    public void setMainPictureUrlMedium(String mainPictureUrlMedium) {
        this.mainPictureUrlMedium = mainPictureUrlMedium;
    }

    public Map<String, Object> getAlternativeTitles() {
        return alternativeTitles;
    }

    public void setAlternativeTitles(Map<String, Object> alternativeTitles) {
        this.alternativeTitles = alternativeTitles;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getStartSeasonYear() {
        return startSeasonYear;
    }

    public void setStartSeasonYear(Integer startSeasonYear) {
        this.startSeasonYear = startSeasonYear;
    }

    public String getStartSeasonSeason() {
        return startSeasonSeason;
    }

    public void setStartSeasonSeason(String startSeasonSeason) {
        this.startSeasonSeason = startSeasonSeason;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getPictureUrlsMedium() {
        return pictureUrlsMedium;
    }

    public void setPictureUrlsMedium(List<String> pictureUrlsMedium) {
        this.pictureUrlsMedium = pictureUrlsMedium;
    }
}
