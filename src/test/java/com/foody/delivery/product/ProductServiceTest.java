package com.foody.delivery.product;

import com.foody.delivery.common.exception.ResourceNotFoundException;
import com.foody.delivery.product.dto.ProductRequest;
import com.foody.delivery.product.dto.ProductResponse;
import com.foody.delivery.product.model.Product;
import com.foody.delivery.product.model.ProductCategory;
import com.foody.delivery.product.repository.ProductRepository;
import com.foody.delivery.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(
                "Foody Classic Burger",
                "Delicioso hambúrguer bovino de 180g",
                new BigDecimal("29.90"),
                ProductCategory.HAMBURGUER,
                null
        );
        product.setId(1L);
    }

    @Test
    @DisplayName("Deve listar todos os produtos disponíveis")
    void findAll_ShouldReturnProductList() {
        when(productRepository.findByAvailableTrue()).thenReturn(List.of(product));

        List<ProductResponse> result = productService.findAll(true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Foody Classic Burger", result.get(0).name());
        verify(productRepository, times(1)).findByAvailableTrue();
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void findById_ShouldReturnProduct_WhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Foody Classic Burger", result.name());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void findById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(99L));
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve criar um novo produto com sucesso")
    void create_ShouldSaveAndReturnProduct() {
        ProductRequest request = new ProductRequest(
                "Chop de Vinho",
                "Bebida artesanal",
                new BigDecimal("12.00"),
                ProductCategory.BEBIDA,
                null,
                true
        );

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        ProductResponse result = productService.create(request);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("Chop de Vinho", result.name());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve excluir produto por ID")
    void delete_ShouldRemoveProduct_WhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }
}
