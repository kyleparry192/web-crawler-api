package com.kyleparry.web.crawler.api.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public final class UriUtils {

    public static Set<URI> extractUrlsForDomain(final Document document, final URI target) {
        if (null == document) {
            return new HashSet<>();
        }

        final Set<URI> _links = new HashSet<>();

        Elements links = document.select("a[href]");
        for (Element link : links) {
            final String href = link.attr("abs:href");
            if (null == href) {
                continue;
            }

            try {
                URI current = new URI(href);
                if (target.getHost().equalsIgnoreCase(current.getHost())) {
                    _links.add(current);
                }
            } catch (URISyntaxException e) {
                continue;
            }
        }

        return _links;
    }

}
