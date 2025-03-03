package com.meli.challenge.products.unit.service.service;

import com.meli.challenge.products.model.dto.DTOProductMetrics;
import com.meli.challenge.products.model.entity.Product;
import com.meli.challenge.products.repository.ProductRepository;
import com.meli.challenge.products.service.impl.ProductMetricsServiceImpl;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductMetricsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private ProductMetricsServiceImpl productMetricsService;

    @BeforeEach
    void setUp() {
        productMetricsService = new ProductMetricsServiceImpl(productRepository, meterRegistry);
    }

    @Test
    void testRecordCategoryViewWhenCategoryNotExist() {
        String category = "Electronics";
        AtomicInteger mockAtomicInteger = mock(AtomicInteger.class);
        when(meterRegistry.gauge(anyString(), any(AtomicInteger.class))).thenReturn(mockAtomicInteger);

        productMetricsService.recordCategoryView(category);

        verify(meterRegistry).gauge(eq("product.category.views"), any(AtomicInteger.class));
        verify(mockAtomicInteger).incrementAndGet();
    }

    @Test
    void testRecordCategoryViewWhenCategoryExists() {
        String category = "Electronics";
        AtomicInteger existingAtomicInteger = new AtomicInteger(2);
        when(meterRegistry.gauge(eq("product.category.views"), any(AtomicInteger.class)))
                .thenReturn(existingAtomicInteger);

        productMetricsService.recordCategoryView(category);

        assertEquals(3, existingAtomicInteger.get());
        verify(meterRegistry).gauge(eq("product.category.views"), any(AtomicInteger.class));
    }

    @Test
    void testGetProductMetrics() {
        // Arrange
        //AtomicInteger existingAtomicInteger = new AtomicInteger(0);
        when(meterRegistry.gauge(eq("product.category.views"), any(AtomicInteger.class)))
                .thenAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    // Devuelve el argumento AtomicInteger directamente
                    return (AtomicInteger) args[1];
                });
        productMetricsService.recordCategoryView("Balones");
        productMetricsService.recordCategoryView("Balones");
        productMetricsService.recordCategoryView("Accesorios");
        productMetricsService.recordCategoryView("Accesorios");
        productMetricsService.recordCategoryView("Accesorios");
        productMetricsService.recordCategoryView("Ropa");

        DTOProductMetrics metrics = productMetricsService.getProductMetrics();

        // Assert
        List<String> topCategories = metrics.topCategories();
        assertEquals(3, topCategories.size());
        assertEquals(0, topCategories.indexOf("Accesorios"));
        assertEquals(1, topCategories.indexOf("Balones"));
        assertEquals(2, topCategories.indexOf("Ropa"));
    }

}

