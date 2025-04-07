package org.exchange;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

@AllArgsConstructor
public class CurrencyRatesRequestManager {

    private final String API_KEY;

    public CurrencyResponseDAO getRateForCurrencyBySymbol(String currencySymbol) {
        String url = "https://api.currencyfreaks.com/v2.0/rates/latest?apikey=" + API_KEY + "&symbols=" + currencySymbol;

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = (CloseableHttpResponse) client.execute(request)) {

            return mapResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CurrencyResponseDAO mapResponse(CloseableHttpResponse response) throws Exception {
        String json = EntityUtils.toString(response.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, CurrencyResponseDAO.class);
    }

}
