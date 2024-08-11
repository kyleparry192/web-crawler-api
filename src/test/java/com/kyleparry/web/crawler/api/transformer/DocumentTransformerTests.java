package com.kyleparry.web.crawler.api.transformer;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentTransformerTests {

    private DocumentTransformer documentTransformer;

    @BeforeEach
    void setUp() {
        documentTransformer = new DocumentTransformer();
    }

    @Test
    void testToDocument_whenRequestIsNull() {
        // Given
        final String html = null;

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> documentTransformer.toDocument(html));

        // Then
        assertThat(exception.getMessage()).isEqualTo("html is marked non-null but is null");
    }

    @Test
    void testToDocument_whenMaliciousCodeIsRemoved() {
        // Given
        final String html = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";

        // When
        final Document document = documentTransformer.toDocument(html);

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

}
