package com.meli.challenge.products.service;

import com.meli.challenge.products.model.dto.DTOCreateProduct;
import com.meli.challenge.products.model.dto.DTOProduct;
import org.springframework.data.domain.Page;

public interface ProductService {

    DTOProduct createProduct(DTOCreateProduct product);

    Page<DTOProduct> getProductsByCategory(String category, int page, int size);

    DTOProduct getProductById(Long id);

    Page<DTOProduct> getAllProducts(int page, int size);

    DTOProduct updateProduct(DTOProduct dtoProduct);

    void deleteProduct(Long id);

}
