package com.kyleparry.web.crawler.api.service;

import com.kyleparry.web.crawler.api.gateway.WebCrawlerGateway;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

@Service
@AllArgsConstructor
public class WebCrawlerService {

    private final WebCrawlerGateway webCrawlerGateway;

    private final PriorityQueue<String> toBeVisited = new PriorityQueue<>();
    private final Set<String> visited = new HashSet<>();

    public Set<String> findAllUrls(@NonNull final String url) {
        toBeVisited.add(url);

        while (null != toBeVisited.peek()) {
            final String target = toBeVisited.poll();
            if (visited.contains(target)) {
                continue;
            }

            final String body = webCrawlerGateway.fetchBody(target);

            visited.add(target);
        }


        return new HashSet<>();
    }

}
