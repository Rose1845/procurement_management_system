package com.rose.procurement.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcureException extends Throwable {
    /**
     * @param message
     */
    public ProcureException(String message) {
        super(message);
    }
    private Integer statusCode=500;
    private String metadata="No metadata";
    private String message="Internal server error";
}
