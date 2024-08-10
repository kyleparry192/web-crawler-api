package com.kyleparry.web.crawler.api.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class UriUtils {

    private static final String URL_REGEX = "";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public static Set<String> extract(final String value) {
        if (null == value) {
            return new HashSet<>();
        }

        final Matcher matcher = URL_PATTERN.matcher(value);
        final Set<String> links = new HashSet<>();
        while (matcher.find()) {
            final String found = matcher.group();
            final String normalized = normalize(found);
            if (null != normalized) {
                links.add(normalized);
            }
        }
        return links;
    }

    private static String normalize(final String urlString) {
        try {
            return new URI(urlString).toString();
        } catch (final URISyntaxException e) {
            log.error("Failed to create URI from '{}'", urlString, e);
            return null;
        }
    }

}
