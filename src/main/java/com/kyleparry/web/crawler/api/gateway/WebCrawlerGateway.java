package com.kyleparry.web.crawler.api.gateway;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class WebCrawlerGateway {

    private final RestTemplate restTemplate;

    public Optional<String> fetchResponseContent(@NonNull final URI url) {
        try {
            final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                logNonSuccessfulResponseWarning(response.getStatusCode(), url.toString());
                return Optional.empty();
            }
            return Optional.ofNullable(response.getBody());
        } catch (final HttpStatusCodeException e) {
            logNonSuccessfulResponseWarning(e.getStatusCode(), url.toString());
            return Optional.empty();
        }
    }

    private void logNonSuccessfulResponseWarning(final HttpStatusCode status, final String url) {
        log.warn("Received '{}' response from '{}'", status, url);
    }

}
