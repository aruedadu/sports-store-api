package com.meli.challenge.products.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.challenge.products.model.dto.DTOCreateProduct;
import com.meli.challenge.products.model.dto.DTOProduct;
import com.meli.challenge.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    private DTOCreateProduct dtoCreateProduct;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        dtoCreateProduct = new DTOCreateProduct(
                "Balon de Futbol",
                29.99,
                10,
                "Nike",
                List.of("Balones"));
    }

    @Test
    void testGetAllProducts() throws Exception {

        mockMvc.perform(get("/api/products?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value("10"));
    }

    @Test
    void testGetProductById_Success() throws Exception {

        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Balon de Futbol Adidas"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {

        mockMvc.perform(get("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct_Success() throws Exception {

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoCreateProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Balon de Futbol"));
    }

    @Test
    void testUpdateProduct_Success() throws Exception {

        DTOProduct updatedDTO = new DTOProduct(20L,
                "Balon Pro",
                35.99,
                15,
                "Adidas",
                List.of("Balones"));


        mockMvc.perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Balon Pro"));
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }

}



