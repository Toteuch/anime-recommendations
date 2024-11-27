package com.toteuch.animerecommendations.malapi.exception;

import org.springframework.http.HttpStatusCode;

public class MalApiListVisibilityException extends MalApiException {
    public MalApiListVisibilityException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
