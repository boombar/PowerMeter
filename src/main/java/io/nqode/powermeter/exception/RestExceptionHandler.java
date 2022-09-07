package io.nqode.powermeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request){
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, exception, getPath(request)));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException exception, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, exception, getPath(request)));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, exception, getPath(request)));
    }

    @ExceptionHandler(EntityExistException.class)
    public ResponseEntity<ApiError> handleEntityExistException(EntityExistException exception, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, exception, getPath(request)));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}