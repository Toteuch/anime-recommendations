package com.toteuch.animerecommendations.anime.entities;

import jakarta.persistence.*;

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
    private String mainPictureMediumUrl;
    private String status;
    private Date startDate;
    private Date endDate;
    private String source;
    private String rating;
    @OneToMany
    private List<AlternativeTitle> alternativeTitles;
    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;
    @OneToMany
    private List<PictureLink> pictureLinks;


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

    public String getMainPictureMediumUrl() {
        return mainPictureMediumUrl;
    }

    public void setMainPictureMediumUrl(String mainPictureMediumUrl) {
        this.mainPictureMediumUrl = mainPictureMediumUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public List<AlternativeTitle> getAlternativeTitles() {
        return alternativeTitles;
    }

    public void setAlternativeTitles(List<AlternativeTitle> alternativeTitles) {
        this.alternativeTitles = alternativeTitles;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public List<PictureLink> getPictureLinks() {
        return pictureLinks;
    }

    public void setPictureLinks(List<PictureLink> pictureLinks) {
        this.pictureLinks = pictureLinks;
    }
}
