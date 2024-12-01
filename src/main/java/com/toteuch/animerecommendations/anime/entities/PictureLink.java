package com.toteuch.animerecommendations.anime.entities;

import jakarta.persistence.*;

@Entity
public class PictureLink {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String medium;
    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    public PictureLink() {
    }

    public PictureLink(String medium, Anime anime) {
        this.medium = medium;
        this.anime = anime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }
}
