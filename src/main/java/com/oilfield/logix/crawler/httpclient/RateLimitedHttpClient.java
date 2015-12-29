package com.oilfield.logix.crawler.httpclient;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Rate limited {@link HttpClient} to get around dumb rate limiting from the site. Has retry logic.
 * It seems you are allowed roughly 16 requests/min, so 4 seconds should be enough. I'm too lazy to
 * implement the other execute methods I'm not using with rate limiting as well
 *
 * @author Jordan Sanderson
 */
public class RateLimitedHttpClient {

    public static String RATE_LIMIT_STRING = "You have exceeded the maximum number of queries per minute allowed for the public queries.";

    private CloseableHttpClient httpClient;

    public RateLimitedHttpClient() {
        this.httpClient = HttpClientBuilder.create().build();
    }

    public String execute(HttpUriRequest httpUriRequest) throws IOException {
        sleep(4000);
        CloseableHttpResponse response = httpClient.execute(httpUriRequest);
        String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        int i = 1;

        // Retry with increasing backoff if rate limited
        while (responseString.contains(RATE_LIMIT_STRING)) {
            sleep(4000 * i);
            response = httpClient.execute(httpUriRequest);
            responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            i++;
        }
        return responseString;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {

        }
    }
}
