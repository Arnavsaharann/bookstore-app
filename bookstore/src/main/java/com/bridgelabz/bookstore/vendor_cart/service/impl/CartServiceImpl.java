package com.bridgelabz.bookstore.vendor_cart.service.impl;

import com.bridgelabz.bookstore.vendor_cart.dto.BookDTO;
import com.bridgelabz.bookstore.vendor_cart.dto.CartDTO;
import com.bridgelabz.bookstore.vendor_cart.dto.CartItemDTO;
import com.bridgelabz.bookstore.vendor_cart.model.Book;
import com.bridgelabz.bookstore.vendor_cart.model.Cart;
import com.bridgelabz.bookstore.vendor_cart.model.CartItem;
import com.bridgelabz.bookstore.vendor_cart.model.User;
import com.bridgelabz.bookstore.vendor_cart.repository.BookRepository;
import com.bridgelabz.bookstore.vendor_cart.repository.CartItemRepository;
import com.bridgelabz.bookstore.vendor_cart.repository.CartRepository;
import com.bridgelabz.bookstore.vendor_cart.repository.UserRepository;
import com.bridgelabz.bookstore.vendor_cart.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CartDTO addToCart(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository.findByCartAndBook(cart, book)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setBook(book);
                    newItem.setQuantity(0);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);

        return convertToDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO removeFromCart(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        CartItem cartItem = cartItemRepository.findByCartAndBook(cart, book)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        cartItemRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);

        return convertToDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateCartItemQuantity(Long userId, Long bookId, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        CartItem cartItem = cartItemRepository.findByCartAndBook(cart, book)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            cart.getCartItems().remove(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return convertToDTO(cart);
    }

    @Override
    public CartDTO getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        return convertToDTO(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getCartItems().stream()
                .map(item -> new CartItemDTO(
                        item.getCartItemId(),
                        convertToBookDTO(item.getBook()),
                        item.getQuantity()))
                .collect(Collectors.toList());

        BigDecimal totalAmount = cart.getCartItems().stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDTO(cart.getCartId(), itemDTOs, totalAmount);
    }

    private BookDTO convertToBookDTO(Book book) {
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getImageUrl(),
                book.getRating(),
                book.getIsApproved(),
                book.getVendor().getUserId());
    }
}