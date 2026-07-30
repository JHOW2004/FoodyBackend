package com.foody.delivery.product.controller;

import com.foody.delivery.product.dto.ProductRequest;
import com.foody.delivery.product.dto.ProductResponse;
import com.foody.delivery.product.model.ProductCategory;
import com.foody.delivery.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Produtos", description = "Endpoints para consulta e gestão do cardápio de produtos")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna a lista de produtos cadastrados no cardápio")
    public ResponseEntity<List<ProductResponse>> findAll(
            @RequestParam(required = false) Boolean availableOnly
    ) {
        return ResponseEntity.ok(productService.findAll(availableOnly));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Buscar por categoria", description = "Retorna os produtos filtrados por categoria")
    public ResponseEntity<List<ProductResponse>> findByCategory(@PathVariable ProductCategory category) {
        return ResponseEntity.ok(productService.findByCategory(category));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar produto", description = "Cadastra um novo produto no cardápio")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Altera os dados de um produto existente")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto", description = "Remove um produto do cardápio")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
