package com.oilfield.logix.crawler.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
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
    private List<HttpClientContext> contexts;
    private static int NUM_CONTEXTS = 30;
    private static int CURRENT_CLIENT = 0;


    public RateLimitedHttpClient() {
        this.httpClient = HttpClientBuilder.create().build();
        contexts = new ArrayList<>();
        for(int i= 0; i < NUM_CONTEXTS; i++) {
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(new BasicCookieStore());
            contexts.add(context);
        }
    }

    public String execute(HttpUriRequest httpUriRequest) throws IOException {
        CURRENT_CLIENT++;
        if(CURRENT_CLIENT == NUM_CONTEXTS) {
            CURRENT_CLIENT = 0;
        }

        sleep(4000/NUM_CONTEXTS);
        CloseableHttpResponse response = httpClient.execute(httpUriRequest, contexts.get(CURRENT_CLIENT));
        String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        int i = 1;
        // Retry with increasing backoff if rate limited
        while (responseString.contains(RATE_LIMIT_STRING)) {
            sleep(2000 * i);
            response = httpClient.execute(httpUriRequest);
            responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            i++;
        }
        return responseString;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
}
