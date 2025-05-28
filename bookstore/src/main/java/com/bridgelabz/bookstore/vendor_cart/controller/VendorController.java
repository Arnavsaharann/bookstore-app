package com.bridgelabz.bookstore.vendor_cart.controller;

import com.bridgelabz.bookstore.vendor_cart.dto.BookDTO;
import com.bridgelabz.bookstore.vendor_cart.model.User;
import com.bridgelabz.bookstore.vendor_cart.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
@Tag(name = "Vendor Operations", description = "APIs for vendor operations")
public class VendorController {
    private final VendorService vendorService;

    @PostMapping("/signup")
    @Operation(summary = "Vendor signup", description = "Register a new vendor")
    @ApiResponse(responseCode = "200", description = "Vendor registered successfully")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return ResponseEntity.ok(vendorService.registerVendor(user));
    }

    @PostMapping("/books")
    @Operation(summary = "Add a new book", description = "Allows a vendor to add a new book")
    @ApiResponse(responseCode = "200", description = "Book added successfully")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO, @RequestParam Long vendorId) {
        return ResponseEntity.ok(vendorService.addBook(bookDTO, vendorId));
    }

    @DeleteMapping("/books/{bookId}")
    @Operation(summary = "Delete a book", description = "Allows a vendor to delete their book")
    @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId, @RequestParam Long vendorId) {
        vendorService.deleteBook(bookId, vendorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/books/{bookId}")
    @Operation(summary = "Update book information", description = "Allows a vendor to update their book details")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookDTO bookDTO,
            @RequestParam Long vendorId) {
        return ResponseEntity.ok(vendorService.updateBook(bookId, bookDTO, vendorId));
    }

    @PostMapping("/books/{bookId}/image")
    @Operation(summary = "Upload book image", description = "Allows a vendor to upload an image for their book")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<String> uploadBookImage(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file,
            @RequestParam Long vendorId) {
        String fileName = vendorService.uploadBookImage(bookId, file, vendorId);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/books")
    @Operation(summary = "Get all books", description = "Retrieves all approved books")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(vendorService.getAllBooks());
    }

    @GetMapping("/books/vendor/{vendorId}")
    @Operation(summary = "Get vendor's books", description = "Retrieves all books for a specific vendor")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    public ResponseEntity<List<BookDTO>> getVendorBooks(@PathVariable Long vendorId) {
        return ResponseEntity.ok(vendorService.getVendorBooks(vendorId));
    }

    @PutMapping("/{vendorId}/account-details")
    @Operation(summary = "Update vendor account details", description = "Allows a vendor to update their account details")
    @ApiResponse(responseCode = "200", description = "Account details updated successfully")
    public ResponseEntity<Void> updateVendorDetails(
            @PathVariable Long vendorId,
            @RequestParam String accountDetails) {
        vendorService.updateVendorDetails(vendorId, accountDetails);
        return ResponseEntity.ok().build();
    }
}