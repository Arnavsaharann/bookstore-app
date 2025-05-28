package com.bridgelabz.bookstore.vendor_cart.controller;

import com.bridgelabz.bookstore.vendor_cart.dto.CartDTO;
import com.bridgelabz.bookstore.vendor_cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Operations", description = "APIs for cart operations")
public class CartController {
    private final CartService cartService;

    @PostMapping("/books/{bookId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to cart", description = "Allows a user to add a book to their cart")
    @ApiResponse(responseCode = "200", description = "Book added to cart successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable Long bookId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(cartService.addToCart(userId, bookId));
    }

    @DeleteMapping("/books/{bookId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove book from cart", description = "Allows a user to remove a book from their cart")
    @ApiResponse(responseCode = "200", description = "Book removed from cart successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Book not found in cart")
    public ResponseEntity<CartDTO> removeFromCart(
            @PathVariable Long bookId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, bookId));
    }

    @PutMapping("/books/{bookId}/quantity")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update book quantity", description = "Allows a user to update the quantity of a book in their cart")
    @ApiResponse(responseCode = "200", description = "Quantity updated successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Book not found in cart")
    public ResponseEntity<CartDTO> updateCartItemQuantity(
            @PathVariable Long bookId,
            @RequestParam Long userId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(userId, bookId, quantity));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get user's cart", description = "Retrieves the cart for the specified user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<CartDTO> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}