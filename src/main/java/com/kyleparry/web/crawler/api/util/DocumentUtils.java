package com.kyleparry.web.crawler.api.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public final class DocumentUtils {

    private static final String HREF_CSS_QUERY = "a[href]";
    private static final String HREF_ATTRIBUTE_KEY = "abs:href";

    public static Document parse(@NonNull final String bodyHtml) {
        final String cleanHtml = Jsoup.clean(bodyHtml, Safelist.basic());
        return Jsoup.parseBodyFragment(cleanHtml);
    }

    public static Set<URI> extractUrlsForDomain(@NonNull final Document document, @NonNull final URI url) {
        final Set<URI> urls = new HashSet<>();
        for (final Element element : document.select(HREF_CSS_QUERY)) {
            final String href = element.attr(HREF_ATTRIBUTE_KEY);
            try {
                final URI current = UriUtils.normalize(href);
                if (url.getHost().equalsIgnoreCase(current.getHost())) {
                    urls.add(current);
                }
            } catch (final URISyntaxException e) {
                log.error("Failed to normalize url with value '{}'", href, e);
            }
        }
        return urls;
    }

}
