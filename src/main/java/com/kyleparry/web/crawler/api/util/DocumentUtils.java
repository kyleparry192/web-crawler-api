package com.kyleparry.web.crawler.api.util;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public final class DocumentUtils {

    private static final String HREF_CSS_QUERY = "a[href]";
    private static final String HREF_ATTRIBUTE_KEY = "abs:href";

    public static Set<URI> extractUrlsForDomain(@NotNull final Document document, @NotNull final URI target) {
        if (null == document || null == target) {
            return new HashSet<>();
        }

        final Set<URI> urls = new HashSet<>();
        for (final Element element : document.select(HREF_CSS_QUERY)) {
            final String href = element.attr(HREF_ATTRIBUTE_KEY);
            try {
                final URI current = UriUtils.normalize(href);
                if (target.getHost().equalsIgnoreCase(current.getHost())) {
                    urls.add(current);
                }
            } catch (final URISyntaxException e) {
                log.error("Failed to normalize url with value '{}'", href, e);
            }
        }
        return urls;
    }

}
