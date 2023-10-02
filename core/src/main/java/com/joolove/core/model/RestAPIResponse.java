package com.joolove.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class RestAPIResponse<T> {
    private boolean success;
    private T response;
    private RestAPIError error;

    public static <T> RestAPIResponse<T> success(T response) {
        return new Builder<T>(true, response, null).build();
    }

    public static <T> RestAPIResponse<T> error(HttpStatus status, String message) {
        RestAPIError error = new RestAPIError(status.value(), message);
        return new Builder<T>(false, null, error).build();
    }

    @Getter
    @AllArgsConstructor
    public static class Builder<T> {
        private final boolean success;
        private final T response;
        private final RestAPIError error;

        public RestAPIResponse<T> build() {
            return new RestAPIResponse<>(success, response, error);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RestAPIError {
        private int status;
        private String message;
    }
}

