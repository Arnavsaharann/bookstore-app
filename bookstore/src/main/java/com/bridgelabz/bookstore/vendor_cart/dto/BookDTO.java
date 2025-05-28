package com.bridgelabz.bookstore.vendor_cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long bookId;
    private String title;
    private String author;
    private BigDecimal price;
    private String imageUrl;
    private Double rating;
    private Boolean isApproved;
    private Long vendorId;
}