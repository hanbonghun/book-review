package org.example.bookreview.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    // Auth 관련 에러
    INVALID_TOKEN("AUTH_001", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("AUTH_002", "만료된 토큰입니다", HttpStatus.UNAUTHORIZED),
    BLACKLISTED_TOKEN("AUTH_003", "사용할 수 없는 토큰입니다", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("AUTH_004", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISSING("AUTH_005", "리프레시 토큰이 없습니다", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("AUTH_006", "저장된 리프레시 토큰을 찾을 수 없습니다", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISMATCH("AUTH_007", "리프레시 토큰이 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_BLACKLISTED("AUTH-008", "사용할 수 없는 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),

    // User 관련 에러
    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("USER_002", "이미 사용 중인 이메일입니다", HttpStatus.CONFLICT),

    // Book 관련 에러
    BOOK_NOT_FOUND("BOOK_001", "해당 도서를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 일반 에러
    VALIDATION_ERROR("COMMON_001", "입력값이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("COMMON_002", "내부 서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorType(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}