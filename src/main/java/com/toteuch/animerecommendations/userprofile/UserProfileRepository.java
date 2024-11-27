package com.toteuch.animerecommendations.userprofile;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    UserProfile findByUsername(String username);

    List<UserProfile> findByOrderByLastSeenAsc(Limit limit);

    List<UserProfile> findByAnimeRatedCount(int count);

    UserProfile findTopByAnimeListSizeOrderByLastSeenAsc(int animeListSize);

    UserProfile findTopByOrderByLastUpdateAsc();

    UserProfile findTopByAnimeRatedCountGreaterThanAndAffinityIsNullOrderByLastUpdateAsc(int animeRatedCount);

    @Query("Select up from UserProfile up WHERE up.affinityUpdate < up.lastUpdate ")
    UserProfile findAffinityToUpdate();
}
