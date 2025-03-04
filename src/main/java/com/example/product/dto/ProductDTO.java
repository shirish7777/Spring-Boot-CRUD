package com.example.product.dto;

import com.example.category.dto.CategoryDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private CategoryDTO category;
}
