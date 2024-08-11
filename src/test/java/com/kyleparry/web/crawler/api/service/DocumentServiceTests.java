package com.kyleparry.web.crawler.api.service;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTests {

    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        documentService = new DocumentService();
    }

    @Test
    void testParse_whenBodyHtmlIsNull() {
        // Given
        final String html = null;

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> documentService.parse(html));

        // Then
        assertThat(exception.getMessage()).isEqualTo("bodyHtml is marked non-null but is null");
    }

    @Test
    void testParse_whenMaliciousCodeIsRemoved() {
        // Given
        final String html = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";

        // When
        final Document document = documentService.parse(html);

        // Then
        final String expected = """
                <html>
                 <head></head>
                 <body>
                  <p><a href="http://example.com/" rel="nofollow">Link</a></p>
                 </body>
                </html>""";
        assertThat(document.html()).isEqualTo(expected);
    }

    @Test
    void testExtractUrlsForDomain_whenDocumentIsNull() throws Exception {
        // Given
        final Document document = null;
        final URI target = new URI("http://example.com");

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> documentService.extractUrlsForDomain(document, target));

        // Then
        assertThat(exception.getMessage()).isEqualTo("document is marked non-null but is null");
    }

    @Test
    void testExtractUrlsForDomain_whenUrlIsNull() {
        // Given
        final Document document = Document.createShell("https://example.com");
        final URI target = null;

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> documentService.extractUrlsForDomain(document, target));

        // Then
        assertThat(exception.getMessage()).isEqualTo("url is marked non-null but is null");
    }

    @Test
    void testExtractUrlsForDomain_whenUrlsAreExtracted() throws Exception {
        // Given
        final String html = """
                <html>
                <body>
                <p><a href="http://example.com/">Link 1</a></p>
                <p><a href="/about-us">Link 2</a></p>
                <p><a href="/contact-us">Link 3</a></p>
                <p><a href="http://www.example.com/blog">Link 4</a></p>
                <p><a href="http://other.example.com/blog">Link 5</a></p>
                <p><a href="NOT VALID">Link 5</a></p>
                </body>
                </html>
                """;
        final Document document = documentService.parse(html);
        final URI target = new URI("http://example.com");

        // When
        final Set<URI> actual = documentService.extractUrlsForDomain(document, target);

        // Then
        assertThat(actual).extracting(URI::toString)
                .containsExactly("http://example.com");

    }

}
