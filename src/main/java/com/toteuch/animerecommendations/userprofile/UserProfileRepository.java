package com.toteuch.animerecommendations.userprofile;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    UserProfile findByUsername(String username);

    List<UserProfile> findByOrderByAffinityAsc(Limit limit);

    List<UserProfile> findByAnimeRatedCount(int count);

    UserProfile findTopByOrderByLastUpdateAsc();

    UserProfile findTopByLastUpdateIsNullOrderByLastSeen();

    @Query("Select up from UserProfile up WHERE up.lastUpdate < up.lastSeen ORDER BY up.lastSeen ASC LIMIT 1")
    UserProfile findTopByLastUpdateBeforeLastSeenOrderByLastSeen();

    UserProfile findTopByLastUpdateBeforeOrderByLastUpdate(Date limitDate);

    UserProfile findTopByAffinityIsNullAndLastUpdateIsNotNullOrderByLastUpdate();

    @Query("Select up from UserProfile up WHERE up.affinityUpdate < up.lastUpdate ORDER BY up.lastUpdate ASC LIMIT 1")
    UserProfile findTopByAffinityUpdateBeforeLastUpdateOrderByLastUpdate();

    long countByLastUpdateIsNull();

    UserProfile findTopByLastUpdateIsNotNullOrderByLastUpdate();

    UserProfile findTopByOrderByLastSeen();

    UserProfile findTopByAffinityGreaterThanOrderByAffinity(Double affinity, Limit limit);
}
