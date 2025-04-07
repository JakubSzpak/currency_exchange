package org.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyResponseDAO(
        String date,
        String base,
        Map<String, String> rates
) {
}
