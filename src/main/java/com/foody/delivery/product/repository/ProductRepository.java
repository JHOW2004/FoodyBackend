package com.foody.delivery.product.repository;

import com.foody.delivery.product.model.Product;
import com.foody.delivery.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByAvailableTrue();
    List<Product> findByCategory(ProductCategory category);
}
