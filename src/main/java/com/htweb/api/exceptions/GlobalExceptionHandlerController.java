package com.htweb.api.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.htweb.api.dtos.HttpErrorResponse;
import com.htweb.api.exceptions.http.BaseHttpException;
import com.htweb.api.exceptions.http.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorResponse> handleInternalServerError(Exception ex) {
        log.error("Loi nghiem trong: ", ex);
        HttpErrorResponse error = new HttpErrorResponse("INTERNAL_SERVER_ERROR", "Internal server error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<HttpErrorResponse> handleInternalException(InternalServerException ex) {
        log.error("Loi nghiem trong: ", ex);
        HttpErrorResponse error = new HttpErrorResponse(ex.getCode(), "Internal server error");
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<HttpErrorResponse> handleHttpException(BaseHttpException ex) {
        HttpErrorResponse error = new HttpErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        HttpErrorResponse res = new HttpErrorResponse("INVALID_DATA", "Invalid request data", errors);
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, Object> errors = new HashMap<>();
        Throwable cause = ex.getCause();

        switch (cause) {
            case com.fasterxml.jackson.databind.exc.InvalidFormatException ife -> {
                String field = ife.getPath().stream()
                        .map(JsonMappingException.Reference::getFieldName)
                        .reduce((first, second) -> second)
                        .orElse("unknown");

                errors.put(field, "Invalid value format");

            }
            case com.fasterxml.jackson.databind.exc.MismatchedInputException mie -> {
                String field = mie.getPath().stream()
                        .map(JsonMappingException.Reference::getFieldName)
                        .reduce((first, second) -> second)
                        .orElse("unknown");

                errors.put(field, "Missing or invalid field");
            }
            case com.fasterxml.jackson.core.JsonParseException jsonParseException ->
                    errors.put("body", "Malformed JSON");

            case null, default -> errors.put("body", "Unreadable request body");
        }

        HttpErrorResponse res = new HttpErrorResponse(
                "INVALID_DATA",
                "Request body is invalid",
                errors
        );

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<HttpErrorResponse> handleAccessDenied(AuthorizationDeniedException ex) {
        log.warn("Access Denied: {}", ex.getMessage());

        HttpErrorResponse error = new HttpErrorResponse(
                "FORBIDDEN",
                "Access Denied: You don't have permission to access this resource"
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
