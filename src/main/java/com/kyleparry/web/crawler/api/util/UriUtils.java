package com.kyleparry.web.crawler.api.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

public final class UriUtils {

    private static final String FORWARD_SLASH = "/";

    public static URI normalize(@NonNull final String url) throws URISyntaxException {
        final String formatted = StringUtils.removeEnd(url, FORWARD_SLASH);
        return new URI(formatted);
    }

}
