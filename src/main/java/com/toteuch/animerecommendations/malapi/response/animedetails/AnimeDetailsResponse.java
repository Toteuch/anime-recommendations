package com.toteuch.animerecommendations.malapi.response.animedetails;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimeDetailsResponse {
    private Integer id;
    private String title;
    @JsonProperty("media_type")
    private String mediaType;
    private GenreResponse[] genres;
    private String status;
    private Double mean;
    @JsonProperty("num_episodes")
    private Integer numEpisodes;
    @JsonProperty("related_anime")
    private RelatedAnime[] relatedAnimes;

    public AnimeDetailsResponse() {
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

    public GenreResponse[] getGenres() {
        return genres;
    }

    public void setGenres(GenreResponse[] genres) {
        this.genres = genres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Integer getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(Integer numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public RelatedAnime[] getRelatedAnimes() {
        return relatedAnimes;
    }

    public void setRelatedAnimes(RelatedAnime[] relatedAnimes) {
        this.relatedAnimes = relatedAnimes;
    }
}
