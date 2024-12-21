package org.example.bookreview.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.config.NaverApiConfig;
import org.example.bookreview.dto.NaverBookSearchResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class NaverBookSearchService {
    private final RestTemplate restTemplate;
    private final NaverApiConfig naverApiConfig;

    public NaverBookSearchResponse searchBooks(String query, int display, int start, String sort) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverApiConfig.getClientId());
        headers.set("X-Naver-Client-Secret", naverApiConfig.getClientSecret());

        String url = UriComponentsBuilder.fromUriString(naverApiConfig.getUrl())
                .queryParam("query", query)
                .queryParam("display", display)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .encode()
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<NaverBookSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NaverBookSearchResponse.class
        );

        return response.getBody();
    }
}