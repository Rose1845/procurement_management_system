package com.rose.procurement.supplier.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.utils.address.Address;
import lombok.RequiredArgsConstructor;
import lombok.experimental.WithBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SpringBootTest
class SupplierControllerTest {
    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private  SupplierRepository supplierRepository;


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
    @WithMockUser
    void createSupplier() throws Exception {
        Supplier supplierDto = Supplier.builder()
                .vendorId(UUID.randomUUID().toString())
                .name("roses")
                .email("rose34@gmail.com")
                .phoneNumber("071237856")
                .contactInformation("test3")
                .contactPerson("rose67")
                .termsAndConditions("testwerrr")
                .paymentType(PaymentType.valueOf("MPESA"))
                .address(new Address("wer","erff","ross","wed"))
                .build();
        ResultActions response = mockMvc.perform(post("/api/v1/suppliers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierDto)));
        response.andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAllSuppliers() throws Exception {
        List<Supplier> supplierList =  supplierRepository.findAll();

        ResultActions resultActions = mockMvc.perform(get("/api/v1/suppliers"));
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.size()",
                                is(supplierList.size()))
                );
    }

    @Test
    @WithMockUser
    void getSupplier() {
    }

    @Test
    @WithMockUser
    void updateSupplier() {
    }

    @Test
    @WithMockUser
    void deleteSupplier() {
    }
}