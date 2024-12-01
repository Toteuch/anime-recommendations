package com.toteuch.animerecommendations.useranimescore;

import com.toteuch.animerecommendations.anime.entities.Anime;
import com.toteuch.animerecommendations.userprofile.UserProfile;
import jakarta.persistence.*;

@Entity
public class UserAnimeScore {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "username")
    private UserProfile userProfile;
    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;
    private Integer score;

    public UserAnimeScore() {
    }

    public UserAnimeScore(UserProfile userProfile, Anime anime, Integer score) {
        this.userProfile = userProfile;
        this.anime = anime;
        this.score = score;
    }

    public UserAnimeScore(UserProfile userProfile, Anime anime) {
        this.userProfile = userProfile;
        this.anime = anime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
