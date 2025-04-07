package org.exchange;

public enum ApiConfig {
    API_KEY("690c7c520dbc41d59376f08e33ac5764");

    private final String apiKey;

    ApiConfig(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getKey() {
        return apiKey;
    }
}