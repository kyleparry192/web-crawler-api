package com.kyleparry.web.crawler.api.controller;

import com.kyleparry.web.crawler.api.controller.dto.CrawlRequest;
import com.kyleparry.web.crawler.api.controller.dto.CrawlResponse;
import com.kyleparry.web.crawler.api.service.WebCrawlerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class WebCrawlerController {

    private final WebCrawlerService webCrawlerService;

    @PostMapping(value = "/query", consumes = "application/json", produces = "application/json")
    @SneakyThrows
    public CrawlResponse query(@RequestBody @Valid final CrawlRequest request) {
        final List<String> links = webCrawlerService.findAllUrls(new URI(request.getOrigin())).stream()
                .map(URI::toString)
                .sorted()
                .toList();
        return CrawlResponse.builder()
                .origin(request.getOrigin())
                .links(links)
                .build();
    }

}
