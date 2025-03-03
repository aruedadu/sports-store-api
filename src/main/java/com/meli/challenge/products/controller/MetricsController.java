package com.meli.challenge.products.controller;

import com.meli.challenge.products.service.ProductMetricsService;
import com.meli.challenge.products.model.dto.DTOProductMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
@Tag(name = "Métricas", description = "Métricas generales de los productos")
public class MetricsController {

    private final ProductMetricsService productMetricsService;

    @Operation(summary = "Obtener métricas de productos",
            description = "Retorna estadísticas generales sobre los productos.")
    @GetMapping
    public ResponseEntity<DTOProductMetrics> getProductMetrics() {

        return ResponseEntity.ok(productMetricsService.getProductMetrics());

    }
}
