package com.toteuch.animerecommendations.userprofile;

import com.toteuch.animerecommendations.useranimescore.UserAnimeScore;
import com.toteuch.animerecommendations.useranimescore.UserAnimeScoreRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);
    @Autowired
    private ParseUserList parseUserlist;
    @Autowired
    private UserProfileRepository repo;
    @Autowired
    private UserAnimeScoreRepository userAnimeScoreRepository;
    @Value("${app.userprofile.userList.countLimit}")
    private long userProfileLimitCount;
    @Value("${app.userprofile.referenceUser}")
    private String referenceUsername;

    private int purgeUserProfiles() {
        int deleted = 0;
        List<UserProfile> userProfileToDelete = repo.findByAnimeRatedCount(0);
        userProfileToDelete.forEach(u -> repo.delete(u));
        deleted += userProfileToDelete.size();
        long userProfileCount = repo.count();
        log.debug("UserProfile count limit is set to {}", userProfileLimitCount);

        if (userProfileCount > userProfileLimitCount) {
            long countToDelete = userProfileCount - userProfileLimitCount;
            log.debug("UserProfile count is {}, now deleting {} records", userProfileCount, countToDelete);
            userProfileToDelete = repo.findByOrderByLastSeenAsc(Limit.of((int) countToDelete));
            userProfileToDelete.forEach(u -> repo.delete(u));
            deleted += userProfileToDelete.size();
        } else {
            log.debug("UserProfile count is {}", userProfileCount);
        }
        return deleted;
    }

    public void retrieveAndPurgeUserProfile() {
        int deleted = purgeUserProfiles();
        int created = 0;
        int updated = 0;
        List<String> usernames = parseUserlist.getUsernamesFromMAL();
        for (String username : usernames) {
            UserProfile existing = repo.findByUsername(username);
            if (null == existing) {
                UserProfile userProfile = new UserProfile(username);
                userProfile = repo.save(userProfile);
                created++;
                log.debug("new userProfile saved : {}", userProfile.getUsername());
            } else {
                log.debug("username already exists : {}", existing.getUsername());
                existing.setLastSeen(new Date());
                repo.save(existing);
                updated++;
            }
        }
        log.info("UserProfile created : {} | UserProfile updated : {} | UserProfile deleted : {}", created, updated, deleted);
    }

    public void calculateAffinity(UserProfile userProfile) {
        UserProfile userProfileReference = repo.findByUsername(referenceUsername);
        List<UserAnimeScore> referenceAnimeList = userAnimeScoreRepository.findByUserProfile(userProfileReference);
        List<Double> animeAffinities = new ArrayList<>();
        for (UserAnimeScore referenceScore : referenceAnimeList) {
            UserAnimeScore userScore = userAnimeScoreRepository.findByUserProfileAndAnime(userProfile, referenceScore.getAnime());
            if (userScore != null) {
                Double affinity = null;
                if (userScore.getScore() > referenceScore.getScore()) {
                    affinity = referenceScore.getScore().doubleValue() / userScore.getScore().doubleValue();
                } else if (userScore.getScore() < referenceScore.getScore()) {
                    affinity = userScore.getScore().doubleValue() / referenceScore.getScore().doubleValue();
                } else {
                    affinity = userScore.getScore().doubleValue() / referenceScore.getScore().doubleValue();
                }
                animeAffinities.add(affinity);
            }
        }
        Double userAffinity = 0.00;
        for (Double animeAffinity : animeAffinities) {
            userAffinity += animeAffinity;
        }
        if (!animeAffinities.isEmpty()) {
            double ponderate = Double.valueOf(animeAffinities.size()) / Double.valueOf(referenceAnimeList.size());
            userAffinity = userAffinity / animeAffinities.size() * ponderate;
        }
        userProfile.setAffinity(userAffinity);
        userProfile.setAffinityUpdate(new Date());
        repo.save(userProfile);
        log.debug("Affinity with user {} is {}", userProfile.getUsername(), userProfile.getAffinity());
    }

    @PostConstruct
    private void initializeUserProfile() {
        UserProfile userProfile = repo.findByUsername(referenceUsername);
        if (userProfile == null) {
            userProfile = new UserProfile(referenceUsername);
            userProfile.setLastSeen(new Date());
            repo.save(userProfile);
        }
    }
}
