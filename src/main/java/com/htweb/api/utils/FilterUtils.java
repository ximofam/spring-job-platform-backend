package com.htweb.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htweb.api.dtos.HttpErrorResponse;
import com.htweb.api.exceptions.http.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FilterUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static void writeResponse(HttpServletResponse response, int status, HttpErrorResponse error) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), error);
    }

    public static void handleException(HttpServletResponse response, UnauthorizedException ex) throws IOException {
        HttpErrorResponse error = new HttpErrorResponse(ex.getCode(), ex.getMessage());
        writeResponse(response, ex.getStatus().value(), error);
    }

    public static void handleException(HttpServletResponse response, Exception ex) throws IOException {
        log.error("Loi nghiem trong: ", ex);
        HttpErrorResponse error = new HttpErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Internal server error"
        );

        writeResponse(response, 500, error);
    }
}
