package com.kyleparry.web.crawler.api.service;

import com.kyleparry.web.crawler.api.gateway.WebCrawlerGateway;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebCrawlerServiceTests {

    @Mock
    private WebCrawlerGateway webCrawlerGateway;
    @Mock
    private DocumentService documentService;

    private WebCrawlerService webCrawlerService;

    @BeforeEach
    void setUp() {
        webCrawlerService = new WebCrawlerService(webCrawlerGateway, documentService);
    }

    @Test
    void testFetchResponseContent_whenUrlIsNull() {
        // Given
        final URI uri = null;

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> webCrawlerService.findAllUrls(uri));

        // Then
        assertThat(exception.getMessage()).isEqualTo("url is marked non-null but is null");
    }

    @Test
    void testFetchResponseContent_whenResponseContentIsPresentAndChildIsScraped() throws Exception {
        // Given
        final URI url = new URI("https://example.com");

        final String responseContent = "Hello World!";
        when(webCrawlerGateway.fetchResponseContent(any())).thenReturn(Optional.of(responseContent));

        final Document document = Document.createShell(url.toString());
        when(documentService.parse(any())).thenReturn(document);

        final URI childUrl = new URI("https://example.com/about-us");
        final Set<URI> expected = Set.of(url, childUrl);
        when(documentService.extractUrlsForDomain(any(), any())).thenReturn(expected, new HashSet<>());

        // When
        final Set<URI> actual = webCrawlerService.findAllUrls(url);

        // Then
        assertThat(actual).isEqualTo(expected);

        verify(webCrawlerGateway).fetchResponseContent(url);
        verify(webCrawlerGateway).fetchResponseContent(childUrl);
        verify(documentService, times(2)).parse(responseContent);
        verify(documentService, times(2)).extractUrlsForDomain(document, url);
    }

    @Test
    void testFetchResponseContent_whenResponseContentIsEmpty() throws Exception {
        // Given
        final URI url = new URI("https://example.com");

        when(webCrawlerGateway.fetchResponseContent(any())).thenReturn(Optional.empty());

        // When
        final Set<URI> actual = webCrawlerService.findAllUrls(url);

        // Then
        assertThat(actual).containsExactly(url);

        verify(webCrawlerGateway).fetchResponseContent(url);
        verifyNoInteractions(documentService);
    }

}
