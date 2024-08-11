package com.kyleparry.web.crawler.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The requested URL is malformed or invalid")
public class InvalidUrlException extends RuntimeException {
}
