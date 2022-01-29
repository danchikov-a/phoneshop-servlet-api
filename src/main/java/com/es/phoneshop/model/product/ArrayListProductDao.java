package com.es.phoneshop.model.product;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
            Stream<Product> streamToHandle = Stream.of();

            if (query == null || query.isEmpty()) {
                //если запрос пустой, то просто показываем все продукты
                streamToHandle = products.stream();
            } else {
                int querySize = query.split(space).length;
                //кол-во итераций — кол-во слов в запросе
                //смысл алгоритма в том, чтобы добавлять в список по кол-ву совпадений
                for (int i = querySize; i > 0; i--) {
                    int amountOfCoincidences = i;

                    Predicate<Product> searchPredicate = product -> {
                        String[] termsOfProductDescription = product.getDescription().split(space);
                        String[] termsOfQuery = query.split(space);
                        return Arrays.stream(termsOfQuery)
                                .filter(Arrays.asList(termsOfProductDescription)::contains)
                                .count() == amountOfCoincidences;
                    };

                    streamToHandle = Stream.concat(streamToHandle, products.stream()
                            .filter(searchPredicate));
                }
            }
            streamToHandle = sort(streamToHandle, sortField, order);
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
            Comparator<Product> sortFieldComparator = fieldDescription.equals(sortField) ?
                    Comparator.comparing(Product::getDescription) : Comparator.comparing(Product::getPrice);
            if (descOrder.equals(order)) {
                stream = stream.sorted(sortFieldComparator.reversed());
            } else {
                stream = stream.sorted(sortFieldComparator);
            }
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
