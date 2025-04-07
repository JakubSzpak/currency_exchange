package org.exchange;

import java.math.BigDecimal;
import java.util.Map;

public record ConversionResult(
        Map<String, Integer> source,
        Map<String, BigDecimal> target
) {
}
