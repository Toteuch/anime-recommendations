package com.toteuch.animerecommendations.userprofile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class UserProfile {
    @Id
    private String username;

    private Date lastSeen;

    private int animeListSize = -1;
    private int animeRatedCount = -1;
    private Date lastUpdate;
    private Double affinity;
    private Date affinityUpdate;

    public UserProfile() {
    }

    public UserProfile(String username) {
        this.username = username;
        this.lastSeen = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getAnimeListSize() {
        return animeListSize;
    }

    public void setAnimeListSize(int animeListSize) {
        this.animeListSize = animeListSize;
    }

    public int getAnimeRatedCount() {
        return animeRatedCount;
    }

    public void setAnimeRatedCount(int animeRatedCount) {
        this.animeRatedCount = animeRatedCount;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Double getAffinity() {
        return affinity;
    }

    public void setAffinity(Double affinity) {
        this.affinity = affinity;
    }

    public Date getAffinityUpdate() {
        return affinityUpdate;
    }

    public void setAffinityUpdate(Date affinityUpdateDate) {
        this.affinityUpdate = affinityUpdateDate;
    }
}
