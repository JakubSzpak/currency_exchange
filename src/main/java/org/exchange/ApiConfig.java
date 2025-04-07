package org.exchange;

public enum ApiConfig {
    API_KEY("YOUR_API_KEY");

    private final String apiKey;

    ApiConfig(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getKey() {
        return apiKey;
    }
}