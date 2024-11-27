package com.toteuch.animerecommendations.malapi.exception;

import org.springframework.http.HttpStatusCode;

public class MalApiListNotFoundException extends MalApiException {
    public MalApiListNotFoundException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
