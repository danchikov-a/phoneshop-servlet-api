package com.es.phoneshop.service.product.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.product.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Deque;
import java.util.LinkedList;

public class ProductServiceImpl implements ProductService {
    private static final int MAX_RECENT_VIEWED_PRODUCTS = 4;
    private static final String RECENT_VIEWED_PRODUCTS_ATTRIBUTE = ProductServiceImpl.class.getName() + ".product";

    private ProductDao productDao;
    private static ProductServiceImpl instance;

    public static ProductServiceImpl getInstance() {
        if(instance == null){
            instance = new ProductServiceImpl();
        }
        return instance;
    }

    private ProductServiceImpl() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public synchronized Deque<Product> getProducts(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();

        Deque<Product> products = (Deque<Product>) httpSession.getAttribute(RECENT_VIEWED_PRODUCTS_ATTRIBUTE);
        if(products == null){
            products = new LinkedList<>();
            httpSession.setAttribute(RECENT_VIEWED_PRODUCTS_ATTRIBUTE, products);
        }

        return products;
    }

    @Override
    public synchronized void add(Deque<Product> products, long productId){
        Product product = productDao.getProduct(productId);
        boolean isThereSuchProduct = products.stream()
                .anyMatch(productToCheck -> productToCheck.getId() == productId);

        if(!isThereSuchProduct) {
            products.addFirst(product);

            if (products.size() == MAX_RECENT_VIEWED_PRODUCTS) {
                products.removeLast();
            }
        }else{
            products.remove(product);
            products.addFirst(product);
        }
    }

}
