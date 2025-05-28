package com.bridgelabz.bookstore.vendor_cart.repository;

import com.bridgelabz.bookstore.vendor_cart.model.Cart;
import com.bridgelabz.bookstore.vendor_cart.model.CartItem;
import com.bridgelabz.bookstore.vendor_cart.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);
}