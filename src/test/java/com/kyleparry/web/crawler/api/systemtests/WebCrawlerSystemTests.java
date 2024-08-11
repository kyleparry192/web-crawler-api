package com.kyleparry.web.crawler.api.systemtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8090)
public class WebCrawlerSystemTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void testCrawl_whenLinksAreFound() throws Exception {
        // Given
        final String requestContent = """
                {
                    "origin" : "http://localhost:8090"
                }
                """;

        // When
        final ResultActions result = mvc.perform(post("/api/v1/crawl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andDo(MockMvcResultHandlers.print());

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.origin", is("http://localhost:8090")))
                .andExpect(jsonPath("$.links", hasSize(6)))
                .andExpect(jsonPath("$.links[0]", is("http://localhost:8090")))
                .andExpect(jsonPath("$.links[1]", is("http://localhost:8090/about-us")))
                .andExpect(jsonPath("$.links[2]", is("http://localhost:8090/contact-us")))
                .andExpect(jsonPath("$.links[3]", is("http://localhost:8090/not-found")))
                .andExpect(jsonPath("$.links[4]", is("http://localhost:8090/our-history")))
                .andExpect(jsonPath("$.links[5]", is("http://localhost:8090/permanent-redirect")));
    }

}
