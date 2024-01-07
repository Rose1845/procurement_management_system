package com.rose.procurement.advice;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception e){
        ProblemDetail problemDetail = null;
        if(e instanceof BadCredentialsException){
             problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),e.getMessage());
            problemDetail.setProperty("access_denied","Authentication Failure");
        }
        if(e instanceof AccessDeniedException){
             problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),e.getMessage());
            problemDetail.setProperty("access_denied","Not authorosed");
        }
        if(e instanceof SignatureException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),e.getMessage());
            problemDetail.setProperty("access_denied","JWT Signature not valid");
        }

        if(e instanceof ExpiredJwtException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),e.getMessage());
            problemDetail.setProperty("access_denied","JWT expired");
        }

        return problemDetail;
    }

//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        log.info("Handling validation exception: {}", errors);
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
}
