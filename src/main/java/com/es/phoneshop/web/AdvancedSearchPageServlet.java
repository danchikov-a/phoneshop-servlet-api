package com.es.phoneshop.web;

import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.dao.product.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchPageServlet extends HttpServlet {

    private static final String SEARCH_JSP = "/WEB-INF/pages/advancedSearchPage.jsp";
    private static final String PRODUCT_CODE_PARAMETER = "searchProductCode";
    private static final String MIN_PRICE_PARAMETER = "searchMinPrice";
    private static final String MAX_PRICE_PARAMETER = "searchMaxPrice";
    private static final String MIN_STOCK_PARAMETER = "searchMinStock";
    private static final String ATTRIBUTE_PRODUCTS = "products";
    private static final String ERROR_MESSAGE = "Not a number";
    private static final String ERRORS_LIST = "errors";
    private static final String PRODUCTS_SIZE = "productsSize";
    private static final int MIN_PRICE_FIELD = 1;
    private static final int MAX_PRICE_FIELD = 2;
    private static final int MIN_STOCK_FIELD = 3;

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCodeParameter = request.getParameter(PRODUCT_CODE_PARAMETER);
        String minPriceParameter = request.getParameter(MIN_PRICE_PARAMETER);
        String maxPriceParameter = request.getParameter(MAX_PRICE_PARAMETER);
        String minStockParameter = request.getParameter(MIN_STOCK_PARAMETER);
        List<String> errors = new ArrayList<>();

        String productCode = productCodeParameter;
        if(StringUtils.isEmpty(productCodeParameter)){
            productCode = null;
        }

        BigDecimal minPrice = null;
        if(!StringUtils.isEmpty(minPriceParameter)) {
            try {
                minPrice = new BigDecimal(minPriceParameter);
            } catch (NumberFormatException numberFormatException) {
                errors.add(ERROR_MESSAGE);
            }
        }

        BigDecimal maxPrice = null;
        if(!StringUtils.isEmpty(maxPriceParameter)) {
            try {
                maxPrice = new BigDecimal(maxPriceParameter);
            } catch (NumberFormatException numberFormatException) {
                errors.add(ERROR_MESSAGE);
            }
        }

        Integer minStock = null;
        if(!StringUtils.isEmpty(minStockParameter)) {
            try {
                minStock = Integer.parseInt(minStockParameter);
            } catch (NumberFormatException numberFormatException) {
                errors.add(ERROR_MESSAGE);
            }
        }

        if(errors.isEmpty()){
            List<Product> products = productDao.advancedFindProducts(productCode, minPrice, maxPrice, minStock);
            request.setAttribute(ATTRIBUTE_PRODUCTS, products);
            request.setAttribute(PRODUCTS_SIZE, products.size());
        }else{
            request.setAttribute(ERRORS_LIST, errors);
            request.setAttribute(ATTRIBUTE_PRODUCTS, new ArrayList<>());
        }

        request.getRequestDispatcher(SEARCH_JSP).forward(request, response);
    }
}
