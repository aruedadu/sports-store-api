package com.meli.challenge.products.service.impl;

import com.meli.challenge.products.mapper.ProductMapper;
import com.meli.challenge.products.model.dto.DTOCreateProduct;
import com.meli.challenge.products.model.exception.ResourceNotFoundException;
import com.meli.challenge.products.service.ProductMetricsService;
import com.meli.challenge.products.model.dto.DTOProduct;
import com.meli.challenge.products.model.entity.Category;
import com.meli.challenge.products.model.entity.Product;
import com.meli.challenge.products.repository.CategoryRepository;
import com.meli.challenge.products.repository.ProductRepository;
import com.meli.challenge.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String NON_EXISTENT_PRODUCT = "Producto con id %s no encontrado";
    private static final String NON_EXISTENT_CATEGORY = "La categoria %s no encontrado";
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMetricsService productMetricsService;

    @Override
    @Transactional
    public DTOProduct createProduct(DTOCreateProduct dtoProduct) {
        Product product = ProductMapper.INSTANCE.toEntity(dtoProduct);

        Set<Category> categories = new HashSet<>();
        for (String categoryName : dtoProduct.categories()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new ResourceNotFoundException(String
                            .format(NON_EXISTENT_CATEGORY, categoryName)));
            categories.add(category);
        }

        product.setCategories(categories);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toDTO(savedProduct);
    }

    @Override
    public Page<DTOProduct> getProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        //verificar que la categoria exista antes de generar la metrica de consulta
        categoryRepository.findByName(category).orElseThrow(() ->
                new ResourceNotFoundException(String.format(NON_EXISTENT_CATEGORY, category)));
        productMetricsService.recordCategoryView(category);
        Page<DTOProduct> response = productRepository.findByCategories_Name(category, pageable)
                .map(ProductMapper.INSTANCE::toDTO);
        if (response.isEmpty()) {
            throw new ResourceNotFoundException(String.format("La categoria %s no tiene productos registrados",
                    category));
        }
        return response;
    }

    @Override
    public DTOProduct getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(NON_EXISTENT_PRODUCT, id)));
        return ProductMapper.INSTANCE.toDTO(product);
    }

    @Override
    public Page<DTOProduct> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable)
                .map(ProductMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public DTOProduct updateProduct(DTOProduct dtoProduct) {
        Product existingProduct = productRepository.findById(dtoProduct.id())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format(NON_EXISTENT_PRODUCT, dtoProduct.id())));

        ProductMapper.INSTANCE.updateProductFromDTO(dtoProduct, existingProduct);

        Set<Category> categories = new HashSet<>();
        for (String categoryName : dtoProduct.categories()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new ResourceNotFoundException(String
                            .format(NON_EXISTENT_CATEGORY, categoryName)));
            categories.add(category);
        }

        existingProduct.setCategories(categories);
        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.INSTANCE.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(NON_EXISTENT_PRODUCT, id));
        }
        productRepository.deleteById(id);
    }
}

