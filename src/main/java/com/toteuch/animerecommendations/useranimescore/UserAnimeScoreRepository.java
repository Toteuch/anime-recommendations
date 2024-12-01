package com.toteuch.animerecommendations.useranimescore;

import com.toteuch.animerecommendations.anime.entities.Anime;
import com.toteuch.animerecommendations.userprofile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnimeScoreRepository extends JpaRepository<UserAnimeScore, Integer> {
    UserAnimeScore findByUserProfileAndAnime(UserProfile userProfile, Anime anime);

    List<UserAnimeScore> findByUserProfile(UserProfile userProfile);
}
