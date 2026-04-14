package com.htweb.api.exceptions;

import com.htweb.api.dtos.HttpErrorResponse;
import com.htweb.api.exceptions.http.BaseHttpException;
import com.htweb.api.exceptions.http.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
