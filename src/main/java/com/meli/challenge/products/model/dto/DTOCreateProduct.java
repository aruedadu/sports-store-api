package com.meli.challenge.products.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DTOCreateProduct (

        @NotBlank(message = "El nombre del producto no puede estar vacio")
        @Size(max = 50, message = "El nombre del producto no debe exceder los 50 caracteres")
        @Schema(description = "Nombre del producto", example = "Balon de Futbol")
        String name,

        @Positive(message = "El precio debe ser un numero positivo")
        @Schema(description = "Precio del producto", example = "29.99")
        double price,

        @Min(value = 0, message = "El stock no puede ser negativo")
        @Schema(description = "Cantidad disponible del producto", example = "10")
        int stock,

        @NotBlank(message = "La marca no puede estar vacia")
        @Schema(description = "Nombre de la marca del producto", example = "Adidas")
        @Size(max = 50, message = "La marca no debe exceder los 50 caracteres")
        String brand,

        @NotEmpty(message = "Debe especificar al menos una categoria")
        @Schema(description = "Lista de las categorias aplicables al producto",
                example = "[\"Balones\", \"Accesorios\"]")
        List<@NotBlank(message = "El nombre de la categoria no puede estar vacio") String> categories

){}
