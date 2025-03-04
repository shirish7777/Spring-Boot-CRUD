package com.example.product.service.impl;

import com.example.category.dto.CategoryDTO;
import com.example.category.exception.ResourceNotFoundException;
import com.example.category.model.Category;
import com.example.product.dto.ProductDTO;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepo;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        if (productDTO.getCategory() != null) {
            product.setCategory(mapToModel(productDTO.getCategory()));
        }
        product = productRepository.save(product);
        return mapToDTO(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found with id: " + id)
        );
        return mapToDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts(int page, int size) {
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));
        List<ProductDTO> productDTOList = new ArrayList<>();

        for (Product product : productPage.getContent()) {
            productDTOList.add(mapToDTO(product));
        }
        return productDTOList;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found with id: " + id)
        );

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(mapToModel(productDTO.getCategory()));

        return mapToDTO(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO mapToDTO(Product product) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(product.getCategory().getId());
        categoryDTO.setName(product.getCategory().getName());
        categoryDTO.setDescription(product.getCategory().getDescription());

        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), categoryDTO);
    }

    private Category mapToModel(CategoryDTO categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return category;
    }
}
