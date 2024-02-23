package com.rose.procurement.supplier.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierCsvRepresentation;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.mappers.SupplierMapper;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.utils.address.Address;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository
                          ) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierDto createSupplier(SupplierDto supplierRequest) {
        Address address = new Address();
        address.setBox(supplierRequest.getAddress().getBox());
        address.setCity(supplierRequest.getAddress().getCity());
        address.setCountry(supplierRequest.getAddress().getCountry());
        address.setLocation(supplierRequest.getAddress().getLocation());
        Supplier supplier = SupplierMapper.MAPPER.toEntity(supplierRequest);
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setTermsAndConditions(supplierRequest.getTermsAndConditions());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setName(supplierRequest.getName());
        supplier.setPhoneNumber(supplierRequest.getPhoneNumber());
        supplier.setAddress(address);
        supplier.setContactInformation(supplierRequest.getContactInformation());
        supplier.setContactPerson(supplierRequest.getContactPerson());
        supplier.setPaymentType(PaymentType.valueOf(String.valueOf(supplierRequest.getPaymentType().name().toUpperCase())));
        Supplier savedSupplier = supplierRepository.save(supplier);
        return SupplierMapper.MAPPER.toDto(savedSupplier);
    }
    public List<Supplier> getAllSuppliers(){
        return new ArrayList<>(supplierRepository.findAll());
    }

    public Supplier getSupplierById(String vendorId) {
        //return UserMapper.mapToUserDto(user);
        //return modelMapper.map(user, UserDto.class);
        return supplierRepository.findById(vendorId).get();
//        return SupplierMapper.MAPPER.toDto(supplier);
//        return supplierRepository.findByVendorId(vendorId)
//                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + vendorId));
    }

    public Supplier updateSupplier(String vendorId,SupplierDto supplierRequest){
        Supplier supplier1 = supplierRepository.findByVendorId(vendorId).orElseThrow(()-> new IllegalStateException("supplier do not exist"));
        supplier1.setName(supplierRequest.getName());
        supplier1.setAddress(supplierRequest.getAddress());
        supplier1.setEmail(supplierRequest.getEmail());
        supplier1.setContactPerson(supplierRequest.getContactPerson());
        supplier1.setPaymentType(PaymentType.valueOf(supplierRequest.getPaymentType().name().toUpperCase()));
        supplier1.setContactInformation(supplierRequest.getContactInformation());
        supplier1.setPhoneNumber(supplierRequest.getPhoneNumber());
        supplier1.setTermsAndConditions(supplierRequest.getTermsAndConditions());
        return supplierRepository.save(supplier1);
    }

    public String deleteSupplier(String vendorId){
        Optional<Supplier> supplier = supplierRepository.findById(vendorId);
        if(supplier.isPresent()){
            supplierRepository.deleteById(vendorId);
        }else {
            return "suplier with id:" + vendorId + "do not exists";
        }
        return "supplier deleted successfully";
    }
    public String deleteAll(){
        supplierRepository.deleteAll();
        return "deleted all suppliers";
    }

    public Integer uploadSuppliers(MultipartFile file) {
        Set<SupplierDto> suppliers = parseCsv(file);
        supplierRepository.saveAll(convertToEntities(suppliers));
        return suppliers.size();
    }

    private Set<SupplierDto> parseCsv(MultipartFile file) {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<SupplierCsvRepresentation> headerColumnNameMappingStrategy = new HeaderColumnNameMappingStrategy<>();
            headerColumnNameMappingStrategy.setType(SupplierCsvRepresentation.class);
            CsvToBean<SupplierCsvRepresentation> csvToBean = new CsvToBeanBuilder<SupplierCsvRepresentation>(reader).withMappingStrategy(headerColumnNameMappingStrategy).withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true).build();
           return csvToBean.parse().stream().map(csvLine -> SupplierDto.builder()
                            .email(csvLine.getEmail())
                    .name(csvLine.getName())
                    .contactInformation(csvLine.getContactInformation())
                   .contactPerson(csvLine.getContactPerson())
                    .phoneNumber(csvLine.getPhoneNumber())
                    .termsAndConditions(csvLine.getTermsAndConditions())
                    .paymentType(csvLine.getPaymentType())
                    .address(new Address(
                            csvLine.getP_o_box(),
                            csvLine.getCountry(),
                            csvLine.getCity(),
                            csvLine.getLocation()
                    ))
                    .build()).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Supplier> convertToEntities(Set<SupplierDto> supplierDtos) {
        return supplierDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toSet());
    }

    private Supplier convertToEntity(SupplierDto supplierDTO) {
        return Supplier.builder()
                .name(supplierDTO.getName())
                .contactPerson(supplierDTO.getContactPerson())
                .contactInformation(supplierDTO.getContactInformation())
                .address(new Address(
                        supplierDTO.getAddress().getBox(),
                        supplierDTO.getAddress().getCountry(),
                        supplierDTO.getAddress().getCity(),
                        supplierDTO.getAddress().getLocation()
                ))
                .email(supplierDTO.getEmail())
                .phoneNumber(supplierDTO.getPhoneNumber())
                .paymentType(supplierDTO.getPaymentType())
                .termsAndConditions(supplierDTO.getTermsAndConditions())
                .build();
    }

    public InputStreamResource generateTemplate() {
        // Create a CSV string with header and placeholder values for a single supplier
        String csvTemplate = "name,contact_person,contact_info,box,city,location,country,email,phone_number,payment_type,terms";

        // Convert the CSV string to InputStreamResource
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvTemplate.getBytes());
        return new InputStreamResource(inputStream);
    }
}
