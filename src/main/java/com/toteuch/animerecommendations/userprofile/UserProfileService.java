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
import java.util.Calendar;
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
    @Value("${app.userprofile.userList.refreshLimitInDays}")
    private Integer refreshLimitInDays;

    private static Double getAffinity(UserAnimeScore referenceScore, UserAnimeScore userScore) {
        double affinity;
        if (userScore.getScore() > referenceScore.getScore()) {
            affinity = referenceScore.getScore().doubleValue() / userScore.getScore().doubleValue();
        } else if (userScore.getScore() < referenceScore.getScore()) {
            affinity = userScore.getScore().doubleValue() / referenceScore.getScore().doubleValue();
        } else {
            affinity = userScore.getScore().doubleValue() / referenceScore.getScore().doubleValue();
        }
        return affinity;
    }

    public UserProfile getUserAnimeScoreUserToRefresh() {
        // look for user with no anime list
        UserProfile userToRefresh = repo.findTopByLastUpdateIsNullOrderByLastSeen();
        if (userToRefresh == null) {
            // look for user that as been seen after their last anime list update
            userToRefresh = repo.findTopByLastUpdateBeforeLastSeenOrderByLastSeen();
        }
        if (userToRefresh == null) {
            // look for user whom update is older than the refresh limit
            Calendar limitDate = Calendar.getInstance();
            limitDate.add(Calendar.DAY_OF_WEEK, -refreshLimitInDays);
            log.debug("Requesting anime with details older than {}", limitDate.getTime());
            userToRefresh = repo.findTopByLastUpdateBeforeOrderByLastUpdate(limitDate.getTime());
        }
        return userToRefresh;
    }

    public UserProfile getUserAffinityToCompute() {
        // Look for user with no affinity and whom have been updated
        UserProfile userProfile = repo.findTopByAffinityIsNullAndLastUpdateIsNotNullOrderByLastUpdate();
        if (userProfile == null) {
            // Look for user whom affinity update is before last update
            userProfile = repo.findTopByAffinityUpdateBeforeLastUpdateOrderByLastUpdate();
        }
        return userProfile;
    }

    public void computeAffinity(UserProfile userProfile) {
        UserProfile userProfileReference = repo.findByUsername(referenceUsername);
        List<UserAnimeScore> referenceAnimeList = userAnimeScoreRepository.findByUserProfile(userProfileReference);
        List<Double> animeAffinities = new ArrayList<>();
        for (UserAnimeScore referenceScore : referenceAnimeList) {
            UserAnimeScore userScore = userAnimeScoreRepository.findByUserProfileAndAnime(userProfile, referenceScore.getAnime());
            if (userScore != null) {
                Double affinity = getAffinity(referenceScore, userScore);
                animeAffinities.add(affinity);
            }
        }
        Double userAffinity = 0.00;
        for (Double animeAffinity : animeAffinities) {
            userAffinity += animeAffinity;
        }
        if (!animeAffinities.isEmpty()) {
            double ponderer = (double) animeAffinities.size() / (double) referenceAnimeList.size();
            userAffinity = userAffinity / animeAffinities.size() * ponderer;
        }
        userProfile.setAffinity(userAffinity);
        userProfile.setAffinityUpdate(new Date());
        repo.save(userProfile);
        log.info("Affinity with user {} is {}", userProfile.getUsername(), userProfile.getAffinity());
    }

    public void purgeUsers() {
        log.debug("UserProfile count limit is set to {}", userProfileLimitCount);
        long userProfileCount = repo.count();
        log.debug("UserProfile count is {}", userProfileCount);
        if (userProfileCount > userProfileLimitCount) {
            List<UserProfile> userProfileToDelete = repo.findByAnimeRatedCount(0);
            log.info("Deleting {} UserProfile with animeRatedCount set to 0", userProfileToDelete.size());
            userProfileToDelete.forEach(u -> repo.delete(u));
        }
        userProfileCount = repo.count();
        log.debug("UserProfile count is {}", userProfileCount);
        if (userProfileCount > userProfileLimitCount) {
            long countToDelete = userProfileCount - userProfileLimitCount;
            List<UserProfile> userProfileToDelete = repo.findByOrderByAffinityAsc(Limit.of((int) countToDelete));
            log.info("Deleting {} UserProfile with the least affinity with referenceUser", userProfileToDelete.size());
            userProfileToDelete.forEach(u -> repo.delete(u));
        }
        userProfileCount = repo.count();
        log.debug("UserProfile count is {}", userProfileCount);
    }

    public void retrieveUsers() {
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
        log.info("UserProfile created : {} | UserProfile updated : {}", created, updated);
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
