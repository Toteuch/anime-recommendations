package com.toteuch.animerecommendations.anime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Date;
import java.util.List;

@Entity
public class Anime {
    @Id
    private Integer id;
    private String title;
    @ManyToMany
    private List<Genre> genres;
    private String mediaType;
    private Integer numEpisodes;
    private Integer prequelAnimeId;
    private Integer sequelAnimeId;
    private Double animeScore;
    private Date detailsUpdate;

    public Anime() {
    }

    public Anime(Integer id, String title) {
        this.id = id;
        this.title = title;
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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

    public Double getAnimeScore() {
        return animeScore;
    }

    public void setAnimeScore(Double animeScore) {
        this.animeScore = animeScore;
    }

    public Date getDetailsUpdate() {
        return detailsUpdate;
    }

    public void setDetailsUpdate(Date detailsUpdate) {
        this.detailsUpdate = detailsUpdate;
    }
}
