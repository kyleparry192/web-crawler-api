package com.kyleparry.web.crawler.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CrawlRequest {

    @NotBlank
    private final String origin;

}
