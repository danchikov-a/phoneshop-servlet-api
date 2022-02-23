package com.es.phoneshop.service.cart.impl;

import com.es.phoneshop.dao.product.impl.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.NotEnoughStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cart.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CartServiceImpl implements CartService {
    private static final String CART_SESSION = "cartSession";

    private Cart cart;
    private ProductDao productDao;

    private static CartServiceImpl instance;

    public static CartService getInstance(){
        synchronized (CartServiceImpl.class) {
            if (instance == null) {
                instance = new CartServiceImpl();
            }
        }
        return instance;
    }

    private CartServiceImpl() {
        cart = new Cart();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Cart cart = (Cart) httpSession.getAttribute(CART_SESSION);

        if(cart == null){
            cart = new Cart();
            httpSession.setAttribute(CART_SESSION, cart);
        }

        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException {
        if (productId != null) {
            Product product = productDao.getProduct(productId);
            int remainingQuantity = quantity;
            CartItem newCartItem = new CartItem(product, quantity);

            Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                    .filter(cartItemToCheck -> {
                        Product productToCheck = cartItemToCheck.getProduct();
                        Product newProduct = newCartItem.getProduct();
                        return productToCheck.equals(newProduct);
                    })
                    .findFirst();

            boolean suchCartItemIsPresent = optionalCartItem.isPresent();

            if (suchCartItemIsPresent) {
                CartItem cartItem = optionalCartItem.get();
                remainingQuantity += cartItem.getQuantity();
            }

            if (remainingQuantity > product.getStock()) {
                throw new NotEnoughStockException();
            } else {
                if (suchCartItemIsPresent) {
                    CartItem cartItem = optionalCartItem.get();
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                } else {
                    cart.getCartItems().add(newCartItem);
                }
            }
            recalculateCart(cart);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws NotEnoughStockException {
        if (productId != null) {
            Product product = productDao.getProduct(productId);

            if (quantity > product.getStock()) {
                throw new NotEnoughStockException();
            }else{
                CartItem newCartItem = new CartItem(product, quantity);

                Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                        .filter(cartItemToCheck -> {
                            Long productToCheckId = cartItemToCheck.getProduct().getId();
                            Long newProductId = newCartItem.getProduct().getId();
                            return productToCheckId.equals(newProductId);
                        })
                        .findFirst();

                if(optionalCartItem.isPresent()) {
                    CartItem cartItem = optionalCartItem.get();
                    cartItem.setQuantity(quantity);
                }
            }
            recalculateCart(cart);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        if(productId != null){
            List<CartItem> cartItems = cart.getCartItems();
            cartItems.removeIf(cartItem -> {
                        Product product = cartItem.getProduct();
                        return productId.equals(product.getId());
                    }
            );
            recalculateCart(cart);
        }else{
            throw new IllegalArgumentException();
        }
    }

    private void recalculateCart(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        int totalQuantity = cartItems.stream()
                .map(CartItem::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();

        cart.setTotalQuantity(totalQuantity);

        BigDecimal totalCost = cartItems.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    BigDecimal price = product.getPrice();
                    BigDecimal amountOfProduct = BigDecimal.valueOf(item.getQuantity());
                    return price.multiply(amountOfProduct);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalCost(totalCost);
    }

    public synchronized void clearCart(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        cart.getCartItems().clear();
        recalculateCart(cart);

        httpSession.setAttribute(CART_SESSION, cart);
    }
}
