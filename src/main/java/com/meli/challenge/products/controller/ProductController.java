package com.meli.challenge.products.controller;

import com.meli.challenge.products.model.dto.DTOCreateProduct;
import com.meli.challenge.products.model.dto.DTOProduct;
import com.meli.challenge.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos deportivos")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(
            summary = "Crear un nuevo producto",
            description = "Permite registrar un nuevo producto en la tienda y asociarlo con una o más categorías."
    )
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<DTOProduct> createProduct(@Valid @RequestBody DTOCreateProduct dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Obtener productos por categoría con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de productos filtrada por categoría")
    @ApiResponse(responseCode = "400", description = "Parámetros inválidos en la solicitud")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public Page<DTOProduct> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") @Parameter(description = "Número de página") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Tamaño de la página") int size) {
        return productService.getProductsByCategory(category, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public DTOProduct getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public Page<DTOProduct> getAllProducts(
            @RequestParam(defaultValue = "0") @Parameter(description = "Número de página") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Tamaño de la página") int size) {
        return productService.getAllProducts(page, size);
    }

    @Operation(summary = "Actualizar un producto")
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PutMapping
    public ResponseEntity<DTOProduct> updateProduct(@Valid @RequestBody DTOProduct dto) {
        return ResponseEntity.ok(productService.updateProduct(dto));
    }

    @Operation(summary = "Eliminar un producto por ID")
    @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}


