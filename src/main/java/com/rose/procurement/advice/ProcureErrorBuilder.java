package com.rose.procurement.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProcureErrorBuilder {

    private List<String> fields;
    private List<String> errors;
    private List<String> codes;
}
