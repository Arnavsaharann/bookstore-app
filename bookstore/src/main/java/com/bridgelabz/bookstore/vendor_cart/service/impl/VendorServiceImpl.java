package com.bridgelabz.bookstore.vendor_cart.service.impl;

import com.bridgelabz.bookstore.vendor_cart.dto.BookDTO;
import com.bridgelabz.bookstore.vendor_cart.model.Book;
import com.bridgelabz.bookstore.vendor_cart.model.User;
import com.bridgelabz.bookstore.vendor_cart.model.User.UserRole;
import com.bridgelabz.bookstore.vendor_cart.repository.BookRepository;
import com.bridgelabz.bookstore.vendor_cart.repository.UserRepository;
import com.bridgelabz.bookstore.vendor_cart.service.VendorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "uploads/books/";

    @Override
    public User registerVendor(User user) {
        user.setRole(UserRole.VENDOR);
        return userRepository.save(user);
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO, Long vendorId) {
        User vendor = userRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (vendor.getRole() != UserRole.VENDOR) {
            throw new RuntimeException("User is not a vendor");
        }

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPrice(bookDTO.getPrice());
        book.setVendor(vendor);
        book.setIsApproved(false);

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Override
    public void deleteBook(Long bookId, Long vendorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.getVendor().getUserId().equals(vendorId)) {
            throw new RuntimeException("Not authorized to delete this book");
        }

        bookRepository.delete(book);
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO, Long vendorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.getVendor().getUserId().equals(vendorId)) {
            throw new RuntimeException("Not authorized to update this book");
        }

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPrice(bookDTO.getPrice());

        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    @Override
    public String uploadBookImage(Long bookId, MultipartFile file, Long vendorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.getVendor().getUserId().equals(vendorId)) {
            throw new RuntimeException("Not authorized to update this book");
        }

        // TODO: Implement file upload logic
        String imageUrl = "https://example.com/images/" + file.getOriginalFilename();
        book.setImageUrl(imageUrl);
        bookRepository.save(book);
        return imageUrl;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findByIsApprovedTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getVendorBooks(Long vendorId) {
        return bookRepository.findByVendorUserId(vendorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateVendorDetails(Long vendorId, String accountDetails) {
        User vendor = userRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (vendor.getRole() != UserRole.VENDOR) {
            throw new RuntimeException("User is not a vendor");
        }

        // TODO: Implement account details update logic
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPrice(book.getPrice());
        dto.setImageUrl(book.getImageUrl());
        dto.setRating(book.getRating());
        dto.setIsApproved(book.getIsApproved());
        dto.setVendorId(book.getVendor().getUserId());
        return dto;
    }
}