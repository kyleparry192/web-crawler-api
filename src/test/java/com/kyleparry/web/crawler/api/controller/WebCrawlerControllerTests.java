package com.kyleparry.web.crawler.api.controller;

import com.kyleparry.web.crawler.api.service.WebCrawlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.net.URI;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebCrawlerController.class)
public class WebCrawlerControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WebCrawlerService webCrawlerService;

    @Test
    void testFindAllUrls_whenUrlsAreCrawled() throws Exception {
        // Given
        final String requestContent = """
                {
                    "origin" : "https://hello.com"
                }
                """;

        final Set<URI> urls = Set.of(
                new URI("https://hello.com/about-us"),
                new URI("https://hello.com"),
                new URI("https://hello.com/contact")
        );
        when(webCrawlerService.findAllUrls(any())).thenReturn(urls);

        // When
        final ResultActions result = mvc.perform(post("/api/v1/crawl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.origin", is("https://hello.com")))
                .andExpect(jsonPath("$.links", hasSize(3)))
                .andExpect(jsonPath("$.links[0]", is("https://hello.com")))
                .andExpect(jsonPath("$.links[1]", is("https://hello.com/about-us")))
                .andExpect(jsonPath("$.links[2]", is("https://hello.com/contact")));

        verify(webCrawlerService).findAllUrls(new URI("https://hello.com"));
    }

    @Test
    void testFindAllUrls_whenOriginIsNotPresent() throws Exception {
        // Given
        final String requestContent = """
                {
                    "origin" : ""
                }
                """;

        // When
        final ResultActions result = mvc.perform(post("/api/v1/crawl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andDo(MockMvcResultHandlers.print());

        // Then
        result.andExpect(status().isBadRequest());

        verifyNoInteractions(webCrawlerService);
    }

}
