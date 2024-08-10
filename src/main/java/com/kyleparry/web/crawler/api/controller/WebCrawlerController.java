package com.kyleparry.web.crawler.api.controller;

import com.kyleparry.web.crawler.api.controller.dto.CrawlRequest;
import com.kyleparry.web.crawler.api.controller.dto.CrawlResponse;
import com.kyleparry.web.crawler.api.service.WebCrawlerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@Validated
public class WebCrawlerController {

    private final WebCrawlerService webCrawlerService;

    @PostMapping(value = "/query", consumes = "application/json", produces = "application/json")
    public CrawlResponse query(@RequestBody @Valid final CrawlRequest request) {
        return CrawlResponse.builder()
                .origin(request.getOrigin())
                .links(new ArrayList<>(webCrawlerService.findAllUrls(request.getOrigin())))
                .build();
    }

}
