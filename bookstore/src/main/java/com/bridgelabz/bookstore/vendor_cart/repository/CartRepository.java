package com.bridgelabz.bookstore.vendor_cart.repository;

import com.bridgelabz.bookstore.vendor_cart.model.Cart;
import com.bridgelabz.bookstore.vendor_cart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}