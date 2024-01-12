package com.rose.procurement.org;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1//organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organization createOrd(@RequestBody @Valid  OrganizationDto organizationDto){
        return organizationService.createOrg(organizationDto);
    }
}
