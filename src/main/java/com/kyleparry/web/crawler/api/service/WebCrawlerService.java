package com.kyleparry.web.crawler.api.service;

import com.kyleparry.web.crawler.api.gateway.WebCrawlerGateway;
import com.kyleparry.web.crawler.api.util.DocumentUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

@Service
@AllArgsConstructor
public class WebCrawlerService {

    private final WebCrawlerGateway webCrawlerGateway;

    public Set<URI> findAllUrls(@NonNull final URI url) {
        final PriorityQueue<URI> toBeVisited = new PriorityQueue<>();
        toBeVisited.add(url);

        final Set<URI> visited = new HashSet<>();

        while (null != toBeVisited.peek()) {
            final URI target = toBeVisited.poll();
            if (visited.contains(target)) {
                continue;
            }

            final Optional<String> responseContent = webCrawlerGateway.fetchResponseContent(target);
            if (responseContent.isPresent()) {
                final Document document = DocumentUtils.parse(responseContent.get());
                final Set<URI> discovered = DocumentUtils.extractUrlsForDomain(document, url);
                toBeVisited.addAll(discovered);
            }

            visited.add(target);
        }

        return visited;
    }

}
