package com.meli.challenge.products.integration.controller;

import com.meli.challenge.products.controller.MetricsController;
import com.meli.challenge.products.model.dto.DTOProductMetrics;
import com.meli.challenge.products.service.ProductMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MetricsController.class)
class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductMetricsService productMetricsService;

    private DTOProductMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new DTOProductMetrics(500, List.of("Ropa", "Accesorios"), 1205, 120.5);
    }

    @Test
    void testGetMetrics_Success() throws Exception {
        when(productMetricsService.getProductMetrics()).thenReturn(metrics);

        mockMvc.perform(get("/api/metrics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_products").value(500))
                .andExpect(jsonPath("$.top_categories[0]").value("Ropa"))
                .andExpect(jsonPath("$.total_stock").value(1205))
                .andExpect(jsonPath("$.average_price").value(120.5));
    }
}
