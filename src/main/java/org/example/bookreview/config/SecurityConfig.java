package org.example.bookreview.config;

import org.example.bookreview.auth.filter.JwtAuthenticationEntryPoint;
import org.example.bookreview.auth.filter.JwtAuthenticationFilter;
import org.example.bookreview.auth.filter.JwtExceptionFilter;
import org.example.bookreview.auth.filter.LogoutFilter;
import org.example.bookreview.oauth.CustomOAuth2UserService;
import org.example.bookreview.oauth.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        CustomOAuth2UserService customOAuth2UserService,
        OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
        JwtAuthenticationFilter jwtAuthenticationFilter,
        LogoutFilter logoutFilter,
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
        JwtExceptionFilter jwtExceptionFilter
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(
                exceptionHandlingCustomizer ->
                    exceptionHandlingCustomizer.authenticationEntryPoint(
                        jwtAuthenticationEntryPoint)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/**", "/", "/api/auth/refresh", "/logout", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .successHandler(oAuth2LoginSuccessHandler)
            )
            .addFilterBefore(logoutFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}