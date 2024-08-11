package com.kyleparry.web.crawler.api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UriUtilsTests {

    @Test
    void testNormalize_whenStringIsNormalized() {
        // Given
        final String urlString = null;

        // When
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> UriUtils.normalize(urlString));

        // Then
        assertThat(exception.getMessage()).isEqualTo("url is marked non-null but is null");
    }

    @ParameterizedTest
    @CsvSource({
            "https://hello.com,https://hello.com",
            "https://hello.com/,https://hello.com"
    })
    void testNormalize_whenStringIsNormalized(final String urlString, final String expected) throws Exception {
        // Given

        // When
        final URI url = UriUtils.normalize(urlString);

        // Then
        assertThat(url.toString()).isEqualTo(expected);
    }

}
