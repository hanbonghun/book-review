package org.example.bookreview.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.auth.service.AuthService;
import org.example.bookreview.common.response.ApiResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ApiResponse<String> refreshAccessToken(
        @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return ApiResponse.success(newAccessToken);
    }
}