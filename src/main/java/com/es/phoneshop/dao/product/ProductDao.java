package com.es.phoneshop.dao.product;

import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts(String query, String sortField, String order);
    List<Product> advancedFindProducts(String productCode, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock);
    void save(Product product);
    void delete(Long id);
}
