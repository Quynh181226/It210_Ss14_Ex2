package com.ss14.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @Column(name = "total_amount")
    private Double totalAmount;

    // BẮT BUỘC PHẢI THÊM 2 DÒNG NÀY
    private Integer quantity;

    @Column(name = "product_id")
    private Long productId;
}