package com.meli.challenge.products.unit.dto;

import com.meli.challenge.products.model.dto.DTOCreateProduct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DTOCreateProductTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDTOCreateProduct() {
        DTOCreateProduct product = new DTOCreateProduct( "Balon de Futbol", 29.99, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidName() {
        DTOCreateProduct product = new DTOCreateProduct( "", 29.99, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El nombre del producto no puede estar vacio", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPrice() {
        DTOCreateProduct product = new DTOCreateProduct( "Balon", -10.0, 10, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El precio debe ser un numero positivo", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidStock() {
        DTOCreateProduct product = new DTOCreateProduct("Balon", 29.99, -5, "Nike", List.of("Balones"));
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El stock no puede ser negativo", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidBrand() {
        DTOCreateProduct product = new DTOCreateProduct( "Balon", 29.99, 10, "", List.of("Balones"));
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("La marca no puede estar vacia", violations.iterator().next().getMessage());
    }

    @Test
    void testEmptyCategories() {
        DTOCreateProduct product = new DTOCreateProduct( "Balon", 29.99, 10, "Nike", List.of());
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("Debe especificar al menos una categoria", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidCategoryName() {
        DTOCreateProduct product = new DTOCreateProduct( "Balon", 29.99, 10, "Nike", List.of(""));
        Set<ConstraintViolation<DTOCreateProduct>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertEquals("El nombre de la categoria no puede estar vacio", violations.iterator().next().getMessage());
    }
}
