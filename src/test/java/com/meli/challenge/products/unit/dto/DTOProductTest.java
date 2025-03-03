package com.meli.challenge.products.unit.dto;

import com.meli.challenge.products.model.dto.DTOProduct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DTOProductTest {

    private static Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testInvalidId() {
        DTOProduct product = new DTOProduct(null, "", 29.99, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El id no puede estar vacio", violations.iterator().next().getMessage());
    }

    @Test
    void testValidDTOProduct() {
        DTOProduct product = new DTOProduct(1L, "Balon de Futbol", 29.99, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidName() {
        DTOProduct product = new DTOProduct(1L, "", 29.99, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El nombre del producto no puede estar vacio", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPrice() {
        DTOProduct product = new DTOProduct(1L, "Balon", -10.0, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El precio debe ser un numero positivo", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidStock() {
        DTOProduct product = new DTOProduct(1L , "Balon", 29.99, -5, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El stock no puede ser negativo", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidBrand() {
        DTOProduct product = new DTOProduct(1L, "Balon", 29.99, 10, "", List.of("Balones"));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("La marca no puede estar vacia", violations.iterator().next().getMessage());
    }

    @Test
    void testEmptyCategories() {
        DTOProduct product = new DTOProduct(1L, "Balon", 29.99, 10, "Nike", List.of());
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("Debe especificar al menos una categoria", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidCategoryName() {
        DTOProduct product = new DTOProduct(1L, "Balon", 29.99, 10, "Nike", List.of(""));
        Set<ConstraintViolation<DTOProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El nombre de la categoria no puede estar vacio", violations.iterator().next().getMessage());
    }
}
