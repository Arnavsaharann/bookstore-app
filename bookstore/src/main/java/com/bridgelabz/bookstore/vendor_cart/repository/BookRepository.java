package com.bridgelabz.bookstore.vendor_cart.repository;

import com.bridgelabz.bookstore.vendor_cart.model.Book;
import com.bridgelabz.bookstore.vendor_cart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByVendor(User vendor);

    List<Book> findByIsApprovedTrue();

    List<Book> findByVendorUserId(Long vendorId);
}