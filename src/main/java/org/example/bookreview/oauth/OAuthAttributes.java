package org.example.bookreview.oauth;

import java.util.Map;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private String providerId;
    private String email;
    private String name;

    public OAuthAttributes(String providerId, String email, String name) {
        this.providerId = providerId;
        this.email = email;
        this.name = name;
    }

    public static OAuthAttributes of(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return ofKakao(attributes);
            case "google":
                return ofGoogle(attributes);
            default:
                throw new IllegalArgumentException("Unsupported OAuth provider: " + provider);
        }
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        String providerId = String.valueOf(attributes.get("id"));
        String email = getNestedValue(attributes, "kakao_account", "email");
        String name = getNestedValue(attributes, "properties", "nickname", "Unknown");
        return new OAuthAttributes(providerId, email, name);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        String providerId = String.valueOf(attributes.get("sub"));
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        return new OAuthAttributes(providerId, email, name);
    }

    private static String getNestedValue(Map<String, Object> attributes, String key, String nestedKey) {
        Object nestedMap = attributes.get(key);
        if (nestedMap instanceof Map) {
            Object value = ((Map<String, Object>) nestedMap).get(nestedKey);
            return value != null ? value.toString() : null;
        }
        return null;
    }

    private static String getNestedValue(Map<String, Object> attributes, String key, String nestedKey, String defaultValue) {
        String value = getNestedValue(attributes, key, nestedKey);
        return value != null ? value : defaultValue;
    }
}