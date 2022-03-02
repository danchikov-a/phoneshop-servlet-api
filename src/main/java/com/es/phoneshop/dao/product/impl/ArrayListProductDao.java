package com.es.phoneshop.dao.product.impl;


import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.NoSuchProductException;
import com.es.phoneshop.model.product.Product;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private final Object lock = new Object();
    private static ProductDao instance;
    private static final String FIELD_DESCRIPTION = "description";
    private static final String DESC_ORDER = "desc";
    private static final String SPACE = " ";

    public static synchronized ProductDao getInstance() {
        if(instance == null){
             instance = new ArrayListProductDao();
        }
        return instance;
    }

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws NoSuchProductException {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            synchronized (lock) {
                return products.stream()
                        .filter(product -> id.equals(product.getId()))
                        .findAny()
                        .orElseThrow(NoSuchProductException::new);
            }
        }
    }

    @Override
    public List<Product> findProducts(String query, String sortField, String sortOrder) {
        synchronized (lock) {
            Stream<Product> streamToHandle;
            List<Product> listToShow = new ArrayList<>();
            if (StringUtils.isEmpty(query)) {
                listToShow.addAll(products);
            } else {
                int querySize = query.split(SPACE).length;
                IntStream.iterate(querySize + 1, i -> i - 1)
                        .limit(querySize + 1)
                        .forEach(i -> {
                            Predicate<Product> searchPredicate = product -> {
                                String[] termsOfProductDescription = product.getDescription().split(SPACE);
                                String[] termsOfQuery = query.split(SPACE);
                                return Arrays.stream(termsOfQuery)
                                        .filter(Arrays.asList(termsOfProductDescription)::contains)
                                        .count() == i;
                            };
                            listToShow.addAll(products.stream()
                                    .filter(searchPredicate)
                                    .collect(Collectors.toList()));
                        });
            }
            streamToHandle = sort(listToShow.stream(), sortField, sortOrder);
            return streamToHandle
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        }
    }

    public Stream<Product> sort(Stream<Product> stream, String sortField, String sortOrder) {
        if (sortField != null) {
            Comparator<Product> sortFieldComparator = FIELD_DESCRIPTION.equals(sortField)
                    ? Comparator.comparing(Product::getDescription)
                    : Comparator.comparing(Product::getPrice);

            stream = DESC_ORDER.equals(sortOrder)
                    ? stream.sorted(sortFieldComparator.reversed())
                    : stream.sorted(sortFieldComparator);
        }
        return stream;
    }

    public List<Product> advancedFindProducts(String productCode, BigDecimal minPrice,
                                              BigDecimal maxPrice, Integer minStock) {
        synchronized (lock) {
            List<Product> listToShow;

            listToShow = products.stream()
                  //  .filter(product -> productCode == null || productCode.equals(product.getCode()))
                    .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                    .filter(product -> minStock == null || product.getStock() >= minStock)
                    .collect(Collectors.toList());

            return listToShow;
        }
    }

    @Override
    public void save(Product product) {
        synchronized (lock) {
            products.add(product);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            synchronized (lock) {
                products.removeIf(product -> id.equals(product.getId()));
            }
        }
    }
}
