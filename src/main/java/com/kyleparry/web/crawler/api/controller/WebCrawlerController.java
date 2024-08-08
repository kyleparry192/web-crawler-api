package com.kyleparry.web.crawler.api.controller;

import com.kyleparry.web.crawler.api.controller.dto.CrawlRecord;
import com.kyleparry.web.crawler.api.controller.dto.CrawlRequest;
import com.kyleparry.web.crawler.api.controller.dto.CrawlResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
public class WebCrawlerController {

    @PostMapping(value = "/query", consumes = "application/json", produces = "application/json")
    public CrawlResponse query(@RequestBody @Valid final CrawlRequest request) {
        final List<CrawlRecord> records = new ArrayList<>();
        records.add(CrawlRecord.builder()
                .source("monzo.com")
                .discovered(List.of("monzo.com/contact"))
                .build());
        return CrawlResponse.builder()
                .origin(request.getOrigin())
                .links(records)
                .build();
    }

}
