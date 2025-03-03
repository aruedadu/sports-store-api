package com.meli.challenge.products.unit.service.service;

import com.meli.challenge.products.model.dto.DTOProduct;
import com.meli.challenge.products.model.entity.Category;
import com.meli.challenge.products.model.entity.Product;
import com.meli.challenge.products.model.exception.ResourceNotFoundException;
import com.meli.challenge.products.repository.CategoryRepository;
import com.meli.challenge.products.repository.ProductRepository;
import com.meli.challenge.products.service.impl.ProductMetricsServiceImpl;
import com.meli.challenge.products.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMetricsServiceImpl metricsService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(1, "Deportes");

        product = Product.builder()
                .id(1L)
                .name("Balon de Futbol")
                .price(29.99)
                .stock(10)
                .brand("Nike")
                .categories(Set.of(category)).build();
    }

    @Test
    void testGetAllProducts() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productPage);

        Page<DTOProduct> result = productService.getAllProducts(0, 10);
        assertEquals(1, result.getTotalElements());
        assertEquals("Balon de Futbol", result.getContent().getFirst().name());
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        DTOProduct result = productService.getProductById(1L);
        assertNotNull(result);
        assertEquals("Balon de Futbol", result.name());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.getProductById(99L));
        assertEquals("Producto con id 99 no encontrado", exception.getMessage());
    }

    @Test
    void testGetProductsByCategory_Success() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        String categoryName = "Deportes";
        when(categoryRepository.findByName(categoryName))
                .thenReturn(Optional.of(new Category(1, categoryName)));
        when(productRepository.findByCategories_Name(eq(categoryName), any(PageRequest.class)))
                .thenReturn(productPage);

        Page<DTOProduct> result = productService.getProductsByCategory(categoryName, 0, 10);
        assertEquals(1, result.getTotalElements());
        assertEquals("Balon de Futbol", result.getContent().getFirst().name());
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Balones"))
                .thenReturn(Optional.of(new Category(1, "Balones")));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        DTOProduct updatedDTO = new DTOProduct(1L, "Balon Pro", 35.99, 15, "Adidas", List.of("Balones"));
        DTOProduct result = productService.updateProduct(updatedDTO);

        assertNotNull(result);
        assertEquals("Balon Pro", result.name());
        assertEquals(35.99, result.price());
        assertEquals(15, result.stock());
        assertEquals("Adidas", result.brand());
    }

    @Test
    void testUpdateProduct_CategoryNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Balones")).thenReturn(Optional.empty());

        DTOProduct updatedDTO = new DTOProduct(1L, "Balon Pro", 35.99, 15, "Adidas", List.of("Balones"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(updatedDTO));
        assertEquals("La categoria Balones no encontrado", exception.getMessage());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        DTOProduct updatedDTO = new DTOProduct(99L, "Balon Pro", 35.99, 15, "Adidas", List.of("Balones"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(updatedDTO));
        assertEquals("Producto con id 99 no encontrado", exception.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.deleteProduct(99L));
        assertEquals("Producto con id 99 no encontrado", exception.getMessage());
    }
}
