package com.kyleparry.web.crawler.api.transformer;

import jakarta.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class DocumentTransformer {

    public Document toDocument(@NotNull final String html) {
        final String cleanHtml = Jsoup.clean(html, Safelist.basic());
        return Jsoup.parseBodyFragment(cleanHtml);
    }

}
