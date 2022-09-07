package io.nqode.powermeter.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
class ApiError {

    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private int status;

    private String error;
    private String message;

    private String path;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }

    ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.message = ex.getMessage();
    }

    ApiError(HttpStatus status, Throwable ex, String path) {
        this(status, ex);
        this.path = path;
    }
}