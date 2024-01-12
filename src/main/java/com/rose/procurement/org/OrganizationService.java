package com.rose.procurement.org;

import com.rose.procurement.utils.address.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    public Organization createOrg(OrganizationDto organizationDto){
        Address address = new Address();
        address.setBox(organizationDto.getAddress().getBox());
        address.setCity(organizationDto.getAddress().getCity());
        address.setCountry(organizationDto.getAddress().getCountry());
        address.setLocation(organizationDto.getAddress().getLocation());
        Organization organization = Organization.builder()
                .name(organizationDto.getName())
                .address(address)
                .phoneNumber(organizationDto.getPhoneNumber()).build();
        return organizationRepository.save(organization);
    }
}
