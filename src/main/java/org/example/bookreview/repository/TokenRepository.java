package org.example.bookreview.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String REFRESH_PREFIX = "refresh:";

    public void addToBlacklist(String token, long timeToLive) {
        redisTemplate.opsForValue()
            .set(BLACKLIST_PREFIX + token, "blacklisted", timeToLive, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    public void saveRefreshToken(String userId, String refreshToken, long timeToLive) {
        redisTemplate.opsForValue()
            .set(REFRESH_PREFIX + userId, refreshToken, timeToLive, TimeUnit.MILLISECONDS);
    }

    public Optional<String> findRefreshToken(String userId) {
        String refreshToken = redisTemplate.opsForValue().get(REFRESH_PREFIX + userId);
        return Optional.ofNullable(refreshToken);
    }
}