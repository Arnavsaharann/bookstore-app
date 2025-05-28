package com.bridgelabz.bookstore.vendor_cart.service;

import com.bridgelabz.bookstore.vendor_cart.dto.CartDTO;

public interface CartService {
    CartDTO addToCart(Long userId, Long bookId);

    CartDTO removeFromCart(Long userId, Long bookId);

    CartDTO updateCartItemQuantity(Long userId, Long bookId, Integer quantity);

    CartDTO getCart(Long userId);
}