package com.oilfield.logix.crawler.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.oilfield.logix.crawler.MainClass;

/**
 * Rate limited {@link HttpClient} to get around dumb rate limiting from the site. Has retry logic.
 * It seems you are allowed roughly 16 requests/min per session, so 4 seconds divided by the number
 * of sessions is the limit. Sessions per hour are limited and sessions expire quickly. The max
 * number of concurrent max-rate sessions allowed is 3. Sessions are switched on a round robin
 * basis.
 *
 * @author Jordan Sanderson
 */
public class RateLimitedHttpClient {

    public static Logger LOGGER = Logger.getLogger(MainClass.class);


    public static String RATE_LIMIT_STRING = "You have exceeded the maximum number of queries per minute allowed for the public queries.";
    public static String SESSION_LIMIT_STRING = "You have exceeded the maximum number of sessions per hour allowed for the public queries.";

    private CloseableHttpClient httpClient;
    private List<HttpClientContext> contexts;
    private static int NUM_CONTEXTS = 3;
    private static int CURRENT_CONTEXT = 0;
    private static int MAX_RETRIES = 15;

    public RateLimitedHttpClient() {
        this.httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(
                        RequestConfig.custom().setConnectionRequestTimeout(4000).build()).build();
        contexts = new ArrayList<>();
        for(int i= 0; i < NUM_CONTEXTS; i++) {
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(new BasicCookieStore());
            contexts.add(context);
        }
    }

    public String execute(HttpUriRequest httpUriRequest) throws IOException {
        sleep(4000/NUM_CONTEXTS);
        CloseableHttpResponse response = handleTimeoutsExecute(httpUriRequest);
        String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        MainClass.lastResponse = responseString;
        int i = 1;

        // Retry with increasing backoff if rate limited
        while (responseString.contains(RATE_LIMIT_STRING) || responseString.contains(SESSION_LIMIT_STRING)) {
            if(responseString.contains(SESSION_LIMIT_STRING)) {
                LOGGER.warn("Session limited, switching sessions and sleeping for " + 2000 * i + " milliseconds");
                incCurrentContext();
            } else {
                LOGGER.warn("Rate limited, sleeping for " + 2000 * i + " milliseconds");
            }
            sleep(2000 * i);

            response = handleTimeoutsExecute(httpUriRequest);
            MainClass.lastResponse = responseString;
            responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            i++;
        }
        incCurrentContext();
        return responseString;
    }

    private CloseableHttpResponse handleTimeoutsExecute(HttpUriRequest httpUriRequest) throws IOException {
        int i = 0;
        while(i < MAX_RETRIES) {
            try {
                return httpClient.execute(httpUriRequest, contexts.get(CURRENT_CONTEXT));
            } catch (IOException e) {
                LOGGER.error(e);
                LOGGER.error("Retrying in ten seconds...");
                sleep(10000);
            }
            i++;
        }
        throw new IOException("Hit max retries");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.warn(e);
        }
    }

    private void incCurrentContext() {
        CURRENT_CONTEXT++;
        if(CURRENT_CONTEXT == NUM_CONTEXTS) {
            CURRENT_CONTEXT = 0;
        }
    }
}
