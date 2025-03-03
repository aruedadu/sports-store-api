package com.meli.challenge.products.service;

import com.meli.challenge.products.model.dto.DTOProductMetrics;

public interface ProductMetricsService {

    void recordCategoryView(String category);
    DTOProductMetrics getProductMetrics();

}
