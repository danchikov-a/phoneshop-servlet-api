package com.es.phoneshop.service.product;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Deque;

public interface ProductService {
    Deque<Product> getProducts(HttpServletRequest request);
    void add(Deque<Product> products, long productId);
}
