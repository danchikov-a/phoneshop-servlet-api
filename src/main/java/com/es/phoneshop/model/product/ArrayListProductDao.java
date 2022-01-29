package com.es.phoneshop.model.product;


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
    public List<Product> findProducts(String query, String sortField, String order) {
        synchronized (lock) {
            String space = " ";
            Stream<Product> streamToHandle;
            List<Product> listToShow = new ArrayList<>();
            if (query == null || query.isEmpty()) {
                listToShow.addAll(products);
            } else {
                int querySize = query.split(space).length;
                IntStream.iterate(querySize + 1, i -> i - 1)
                        .limit(querySize + 1)
                        .forEach(i -> {
                            Predicate<Product> searchPredicate = product -> {
                                String[] termsOfProductDescription = product.getDescription().split(space);
                                String[] termsOfQuery = query.split(space);
                                return Arrays.stream(termsOfQuery)
                                        .filter(Arrays.asList(termsOfProductDescription)::contains)
                                        .count() == i;
                            };
                            listToShow.addAll(products.stream()
                                    .filter(searchPredicate)
                                    .collect(Collectors.toList()));
                        });
            }
            streamToHandle = sort(listToShow.stream(), sortField, order);
            return streamToHandle
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        }
    }

    public Stream<Product> sort(Stream<Product> stream, String sortField, String order) {
        String fieldDescription = "description";
        String descOrder = "desc";
        if (sortField != null) {
            Comparator<Product> sortFieldComparator = fieldDescription.equals(sortField)
                    ? Comparator.comparing(Product::getDescription)
                    : Comparator.comparing(Product::getPrice);

            stream = descOrder.equals(order)
                    ? stream.sorted(sortFieldComparator.reversed())
                    : stream.sorted(sortFieldComparator);
        }
        return stream;
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
