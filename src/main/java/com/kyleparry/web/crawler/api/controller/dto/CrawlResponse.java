package com.kyleparry.web.crawler.api.controller.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class CrawlResponse {

    private final String origin;

    private final List<String> links;

}
