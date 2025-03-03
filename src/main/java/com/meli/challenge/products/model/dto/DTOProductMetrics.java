package com.meli.challenge.products.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DTOProductMetrics(
        @JsonProperty("total_products")
        long totalProducts,
        @JsonProperty("top_categories")
        List<String> topCategories,
        @JsonProperty("total_stock")
        int totalStock,
        @JsonProperty("average_price")
        double averagePrice
) {}

