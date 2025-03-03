package com.meli.challenge.products.service.impl;

import com.meli.challenge.products.model.dto.DTOProductMetrics;
import com.meli.challenge.products.model.entity.Product;
import com.meli.challenge.products.repository.ProductRepository;
import com.meli.challenge.products.service.ProductMetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ProductMetricsServiceImpl implements ProductMetricsService {

    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;

    //se usa concurrent hashmap y atomic integer para evitar condicion de carrera
    private final ConcurrentHashMap<String, AtomicInteger> categoryViews = new ConcurrentHashMap<>();

    public void recordCategoryView(String category) {
        categoryViews.computeIfAbsent(category, k ->
                meterRegistry.gauge("product.category.views",
                        new AtomicInteger(0))).incrementAndGet();
    }

    @Override
    public DTOProductMetrics getProductMetrics() {
        List<Product> products = productRepository.findAll();

        long totalProducts = products.size();
        int totalStock = products.stream().mapToInt(Product::getStock).sum();
        double averagePrice = products.stream().mapToDouble(Product::getPrice).average().orElse(0.0);

        List<String> topCategories = categoryViews.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> -entry.getValue().get()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        return new DTOProductMetrics(totalProducts, topCategories, totalStock, averagePrice);
    }
}
