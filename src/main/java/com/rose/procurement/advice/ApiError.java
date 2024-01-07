package com.rose.procurement.advice;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiError {

        private HttpStatus status;
        private String message;
        private List<String> errors;

    }

