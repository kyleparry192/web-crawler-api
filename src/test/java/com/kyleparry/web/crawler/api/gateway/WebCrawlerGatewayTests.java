package com.kyleparry.web.crawler.api.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WebCrawlerGatewayTests {

    @Autowired
    private WebCrawlerGateway webCrawlerGateway;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testFetchBody_whenUriIsNull() {
        // Given
        final URI uri = null;

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> webCrawlerGateway.fetchBody(uri));

        // Then
        assertThat(exception.getMessage()).isEqualTo("url is marked non-null but is null");
    }

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {
            "OK",
            "CREATED",
            "ACCEPTED",
            "NON_AUTHORITATIVE_INFORMATION",
            "RESET_CONTENT",
            "PARTIAL_CONTENT",
            "MULTI_STATUS",
            "ALREADY_REPORTED",
            "IM_USED"
    }, mode = INCLUDE)
    void testFetchBody_whenResponseContentIsReturned(final HttpStatus status) throws Exception {
        // Given
        final URI uri = new URI("http://localhost:8080/hello");

        final String responseBody = "Hello World!";
        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(status)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(responseBody)
                );

        // When
        final String actual = webCrawlerGateway.fetchBody(uri);

        // Then
        mockServer.verify();
        assertThat(actual).isEqualTo(responseBody);
    }

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {
            "OK",
            "CREATED",
            "ACCEPTED",
            "NON_AUTHORITATIVE_INFORMATION",
            "RESET_CONTENT",
            "PARTIAL_CONTENT",
            "MULTI_STATUS",
            "ALREADY_REPORTED",
            "IM_USED"
    }, mode = EXCLUDE)
    void testFetchBody_whenResponseContentIsNotReturned() throws Exception {
        // Given
        final URI uri = new URI("http://localhost:8080/hello");

        final String responseBody = "Hello World!";
        mockServer.expect(ExpectedCount.once(), requestTo(uri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(responseBody)
                );

        // When
        final String actual = webCrawlerGateway.fetchBody(uri);

        // Then
        mockServer.verify();
        assertThat(actual).isNull();
    }

}
