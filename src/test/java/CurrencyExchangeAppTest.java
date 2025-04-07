import lombok.extern.slf4j.Slf4j;
import org.exchange.ConversionResult;
import org.exchange.CurrencyExchange;
import org.exchange.CurrencyRatesRequestManager;
import org.exchange.CurrencyResponseDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.exchange.ApiConfig.API_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class CurrencyExchangeAppTest {

    protected CurrencyRatesRequestManager manager;

    protected CurrencyExchange exchange;
    

    @BeforeEach
    void setUp() {
        manager = new CurrencyRatesRequestManager(API_KEY.getKey());
        exchange = new CurrencyExchange(manager);
    }

    @Test
    void testGetRates_withValidApiKey_returnsJson() {
        CurrencyResponseDAO currencyResponseDAO = manager.getRateForCurrencyBySymbol("USD");
        assertTrue(currencyResponseDAO.toString().contains("rates"), "Response should contain 'rates'");
        log.info("Response is a valid json: {}", currencyResponseDAO);

    }

    @Test
    void testGetRates_USD_shouldReturn1() {
        CurrencyResponseDAO currencyResponseDAO = manager.getRateForCurrencyBySymbol("USD");
        assertEquals(Map.of("USD", "1.0"), currencyResponseDAO.rates());
        log.info("Currency for USD is expected value: {}", currencyResponseDAO.rates());
    }

    @Test
    void testGetRates_USD_shouldNoTReturn1() {
        CurrencyResponseDAO currencyResponseDAO = manager.getRateForCurrencyBySymbol("EUR");
        assertNotEquals(Map.of("USD", "1.0"), currencyResponseDAO.rates());
        log.info("Currency for USD is not as expected value: {}", currencyResponseDAO.rates());
    }

    @Test
    void exchangeCurrencyTestShouldBeEqual() {
        ConversionResult conversionResult = exchange.exchangeCurrency("USD", "USD", 1);
        assertEquals(conversionResult.source().get("USD").toString(), conversionResult.target().get("USD").toBigInteger().toString());
    }

    @Test
    void shouldConvertPLNToGBPCorrectly() {
        CurrencyRatesRequestManager mockManager = mock(CurrencyRatesRequestManager.class);
        when(mockManager.getRateForCurrencyBySymbol("PLN")).thenReturn(
                new CurrencyResponseDAO(null, "USD", Map.of("PLN", "4.0")));
        when(mockManager.getRateForCurrencyBySymbol("GBP")).thenReturn(
                new CurrencyResponseDAO(null, "USD", Map.of("GBP", "0.8")));
        CurrencyExchange exchange = new CurrencyExchange(mockManager);
        ConversionResult result = exchange.exchangeCurrency("PLN", "GBP", 100);
        assertEquals(Map.of("PLN", 100), result.source());
        assertEquals(Map.of("GBP", new BigDecimal("20.00")), result.target());
    }

    @Test
    void shouldThrowWhenRateMissing() {
        CurrencyRatesRequestManager mockManager = mock(CurrencyRatesRequestManager.class);
        when(mockManager.getRateForCurrencyBySymbol("XYZ")).thenReturn(
                new CurrencyResponseDAO(null, "USD", Map.of()));
        when(mockManager.getRateForCurrencyBySymbol("USD")).thenReturn(
                new CurrencyResponseDAO(null, "USD", Map.of("USD", "1.0")));
        Exception exception = assertThrows(NullPointerException.class, () ->
                exchange.exchangeCurrency("XYZ", "USD", 10)
        );
        assertNotNull(exception);
    }


}
