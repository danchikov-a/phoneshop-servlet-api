package com.es.phoneshop.listener;

import com.es.phoneshop.model.pricehistory.PriceHistoryItem;
import com.es.phoneshop.dao.product.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.product.ProductDao;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.stream.IntStream;

public class DemoDataServletContextListener implements ServletContextListener {
    private ProductDao productDao;
    private static final String CONTEXT_PARAM = "insertDemoData";
    private static final long FIRST_ELEMENT = 1L;
    private static final int SECOND_ELEMENT = 2;
    private static final int LAST_ELEMENT = 14;

    public DemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean isNeedToInsertDemoData = Boolean
                .parseBoolean(servletContextEvent.getServletContext()
                        .getInitParameter(CONTEXT_PARAM));
        if (isNeedToInsertDemoData) {
            saveSampleProducts();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        productDao.getProduct(FIRST_ELEMENT).setPriceHistory(
                new ArrayList<>(Arrays.asList(
                        new PriceHistoryItem(LocalDate.of(1,2,3), productDao.getProduct(FIRST_ELEMENT).getPrice(), usd),
                        new PriceHistoryItem(LocalDate.of(3,2,3), new BigDecimal(100), usd),
                        new PriceHistoryItem(LocalDate.of(4,2,5), new BigDecimal(4), usd)
                ))
        );
        IntStream.range(SECOND_ELEMENT, LAST_ELEMENT).
                forEach(i -> productDao.getProduct((long) i).setPriceHistory(new ArrayList<>(
                                Arrays.asList(new PriceHistoryItem(LocalDate.of(2,2,3), productDao.getProduct((long) i).getPrice(), usd)))
                        ));

    }
}

