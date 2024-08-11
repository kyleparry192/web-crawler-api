package com.kyleparry.web.crawler.api.gateway;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@AllArgsConstructor
@Slf4j
public class WebCrawlerGateway {

    private final RestTemplate restTemplate;

    public String fetchBody(@NonNull final String urlString) {
        try {
            final URI url = new URI(urlString);
            return fetchBody(url);
        } catch (final URISyntaxException e) {
            log.error("Failed to create URI from '{}'", urlString, e);
            return null;
        }
    }

    public String fetchBody(@NonNull final URI url) {
        log.debug("Calling : '{}'", url);
        try {
            final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
            return response.getBody();
        } catch (final HttpStatusCodeException e) {
            log.warn("Received '{}' response from '{}'", e.getStatusCode(), url);
            return null;
        }
    }

}
