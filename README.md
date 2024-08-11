# Web Crawler API

## Brief

Write a simple web crawler in a programming language you're familiar with. Given a starting URL, the crawler should
visit each URL it finds on the same domain. It should print each URL visited, and a list of links found on that page.
The crawler should be limited to one subdomain - so when you start with *https://example.com/*, it would crawl all pages
on the example.com website, but not follow external links, for example to facebook.com or community.example.com.

## API Reference

```http
  POST /api/v1/crawl
```

#### Request

| Parameter | Type     | Description                                            |
|:----------|:---------|:-------------------------------------------------------|
| `origin`  | `string` | **Required**. The URL of the website you wish to crawl |

#### Response

| Parameter | Type     | Description                              |
|:----------|:---------|:-----------------------------------------|
| `origin`  | `string` | The URL of the website which was crawled |
| `links`   | `list`   | A list of all the URLs which were found  |

## Assumptions

- I have considered https://www.example.com and https://example.com to be two different domains.

## Limitations

- The application will not follow links unless the domain is present in the href attribute. For instance,
  `href="/about-us"` would not be crawled but `href="https://example.com/about-us"` would.

## Future Enhancements

- Utilise multi-threading so that the crawling process is not limited to a single thread.
- Follow 308 (Permanent Redirect) responses to the new location.

## Authors

- [@kyleparry192](https://www.github.com/kyleparry192)
