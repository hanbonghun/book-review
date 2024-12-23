package org.example.bookreview.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.example.bookreview.auth.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public String refreshAccessToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        String userId = jwtTokenProvider.getUserId(refreshToken);
        validateStoredRefreshToken(refreshToken, userId);

        return jwtTokenProvider.regenerateAccessToken(refreshToken, new Date());
    }

    private void validateRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new BusinessException(ErrorType.REFRESH_TOKEN_MISSING);
        }

        if (tokenRepository.isBlacklisted(refreshToken)) {
            throw new BusinessException(ErrorType.REFRESH_TOKEN_BLACKLISTED);
        }
    }

    private void validateStoredRefreshToken(String refreshToken, String userId) {
        String storedRefreshToken = tokenRepository.findRefreshToken(userId)
            .orElseThrow(() -> new BusinessException(ErrorType.REFRESH_TOKEN_NOT_FOUND));

        if (!refreshToken.equals(storedRefreshToken)) {
            throw new BusinessException(ErrorType.REFRESH_TOKEN_MISMATCH);
        }
    }
}