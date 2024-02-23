package com.rose.procurement.advice;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProcureErrorBuilder handleInvalidArgument(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        List<String> fields = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            errors.add(e.getDefaultMessage());
            codes.add(e.getCode());
            fields.add(e.getField());
        });
        return ProcureErrorBuilder.builder()
                .errors(errors)
                .codes(codes)
                .fields(fields)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Map<String, String> handleBusinessException(UsernameNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProcureException.class)
    public Map<String, String> handleProcureException(ProcureException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Internal server error");
        errorMap.put("metadata-info", ex.getMetadata() != null ? ex.getMetadata().toString() : "");
        errorMap.put("statusCode", ex.getStatusCode() != null ? ex.getStatusCode().toString() : Integer.valueOf("400").toString());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Map<String, String> handleEntityException(SQLIntegrityConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorCode", ex.getSQLState());
        errorMap.put("message", ex.getMessage());
        errorMap.put("cause", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredJwtException.class)
    public Map<String, String> handleJwtExc(ExpiredJwtException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorCode", "401");
        errorMap.put("message", ex.getMessage());
        errorMap.put("cause", "Session expired");
        return errorMap;
    }
}
