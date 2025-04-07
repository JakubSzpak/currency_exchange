package org.exchange;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class CurrencyExchange {
    private final CurrencyRatesRequestManager manager;

    public ConversionResult exchangeCurrency(String fromCurrency, String toCurrency, Integer givenAmount) {
        Map<String, Integer> sourceCurrencyAndGivenAmount = new HashMap<>();
        Map<String, BigDecimal> destinationCurrencyAndRetrievedAmount = new HashMap<>();

        CurrencyResponseDAO rateForSourceCurrency = manager.getRateForCurrencyBySymbol(fromCurrency);
        CurrencyResponseDAO rateForDestinationCurrency = manager.getRateForCurrencyBySymbol(toCurrency);

        BigDecimal sourceRateToUSD = new BigDecimal(rateForSourceCurrency.rates().get(fromCurrency));
        BigDecimal targetRateToUSD = new BigDecimal(rateForDestinationCurrency.rates().get(toCurrency));

        BigDecimal amountInUSD = BigDecimal.valueOf(givenAmount).divide(sourceRateToUSD, 10, RoundingMode.HALF_UP);
        BigDecimal amountInTargetCurrency = amountInUSD.multiply(targetRateToUSD).setScale(2, RoundingMode.HALF_UP);

        sourceCurrencyAndGivenAmount.put(fromCurrency, givenAmount);
        destinationCurrencyAndRetrievedAmount.put(toCurrency, amountInTargetCurrency);

        log.info("Converted {} {} to {} {}",
                givenAmount, fromCurrency,
                amountInTargetCurrency, toCurrency);
        return new ConversionResult(sourceCurrencyAndGivenAmount, destinationCurrencyAndRetrievedAmount);

    }

}