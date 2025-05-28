package com.bridgelabz.bookstore.vendor_cart.service;

import com.bridgelabz.bookstore.vendor_cart.dto.BookDTO;
import com.bridgelabz.bookstore.vendor_cart.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface VendorService {
    User registerVendor(User user);

    BookDTO addBook(BookDTO bookDTO, Long vendorId);

    void deleteBook(Long bookId, Long vendorId);

    BookDTO updateBook(Long bookId, BookDTO bookDTO, Long vendorId);

    String uploadBookImage(Long bookId, MultipartFile file, Long vendorId);

    List<BookDTO> getAllBooks();

    List<BookDTO> getVendorBooks(Long vendorId);

    void updateVendorDetails(Long vendorId, String accountDetails);
}