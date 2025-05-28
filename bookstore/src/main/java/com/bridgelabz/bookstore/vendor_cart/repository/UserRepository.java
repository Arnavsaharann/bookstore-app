package com.bridgelabz.bookstore.vendor_cart.repository;

import com.bridgelabz.bookstore.vendor_cart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Add custom query methods if needed
}