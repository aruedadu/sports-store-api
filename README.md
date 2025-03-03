
---

## **API REST - Tienda de Deporte**

```
# API REST - Tienda de Deporte

Este proyecto es una API REST desarrollada en **Java 21** con **Spring Boot**, diseñada para la gestión de productos y categorías en una tienda de deporte.  

## Tecnologías Utilizadas
- Java 21  
- Spring Boot  
- Spring Data JPA  
- Springdoc OpenAPI (Swagger)  
- Micrometer & Spring Actuator  
- Base de datos H2 en memoria (decisión por que el equipo en el que fue desarrollada esta api, no cuenta con recursos
suficientes para correr una base de datos local 

## Instalación y Ejecución

### Clonar el Repositorio
```sh
git clone https://github.com/tu-usuario/tu-repositorio.git
cd tu-repositorio
```

### Acceso a la API
- **Swagger UI (Documentación):** [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- **Consola de H2:** [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)
    - **JDBC URL:** `jdbc:h2:mem:challengeDb`
    - **Usuario:** `sa`
    - **Contraseña:** *(vacío)*

## Métricas y Monitoreo

La API incorpora **Spring Actuator con Micrometer**, permitiendo la recopilación y exposición de métricas en tiempo real.

### Top de Categorías Más Consultadas
- Calculado dinámicamente en función de las consultas de productos.
- La métrica se reinicia cada vez que la aplicación se reinicia.
- **Endpoint de métricas:**
  ```
  GET /api/metrics
  ```
  **Ejemplo de respuesta:**
  ```json
  {
    "total_products": 500,
    "top_categories": ["Balones", "Accesorios"],
    "total_stock": 1205,
    "average_price": 120.5
  }
  ```

## Endpoints Disponibles

### Productos
| Método  | Endpoint                            | Descripción                                             |
|---------|-------------------------------------|---------------------------------------------------------|
| GET     | `/api/products`                     | Obtiene todos los productos (paginado).                 |
| GET     | `/api/products/{id}`                | Obtiene un producto por ID.                             |
| GET     | `/api/products/category/{category}` | Obtiene una lista de productos por categoria (paginado) |
| POST    | `/api/products`                     | Crea un nuevo producto.                                 |
| PUT     | `/api/products/{id}`                | Actualiza un producto existente.                        |
| DELETE  | `/api/products/{id}`                | Elimina un producto.                                    |

## Consideraciones sobre los endpoint de productos

los endpoint paginados tienen dos parametros adicionales que son:
- page que indica el numero de pagina a consultar (cuyo valor por defecto es 0) haciendo que el llamado completo se vea asi: /api/products/{id}?page={page}&size={size}
- size que indica el tamaño de la pagina a responder (cuyo valor por defecto es 10) haciendo que el llamado completo se vea asi: /api/products/category/{category}?page={page}&size={size}

### Métricas
| Método  | Endpoint      | Descripción                        |
|---------|--------------|------------------------------------|
| GET     | `/api/metrics` | Obtiene métricas generales de la API. |

## Consideraciones sobre la Base de Datos

Debido a limitaciones de recursos en el entorno de desarrollo, se ha optado por utilizar **H2 en memoria** como base de datos. Esta configuración permite realizar pruebas sin requerir una instalación adicional de bases de datos externas.

El proyecto cuenta con un archivo data.sql que realiza una precarga de datos para poder ver los endopint funcionando de mejor manera.

## Licencia
Este proyecto está distribuido bajo la licencia **MIT**.

#### archivo generado con el apoyo de una IA.