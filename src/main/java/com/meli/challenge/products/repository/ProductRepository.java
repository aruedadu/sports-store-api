package com.meli.challenge.products.repository;


import com.meli.challenge.products.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName")
    Page<Product> findByCategories_Name(String categoryName, Pageable pageable);
}

