package com.rose.procurement.advice;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
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
            problemDetail.setProperty("access_denied","Not authorised");
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
}



