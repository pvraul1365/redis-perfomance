package com.rperezv.redis.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * Product
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 13/09/2025 - 15:44
 * @since 1.17
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    private Integer id;

    private String description;
    private Double price;

}
