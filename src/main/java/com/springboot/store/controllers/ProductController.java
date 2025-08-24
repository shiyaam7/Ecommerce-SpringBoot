package com.springboot.store.controllers;

import com.springboot.store.dtos.ProductDto;
import com.springboot.store.dtos.RegisterProductRequest;
import com.springboot.store.entities.Product;
import com.springboot.store.mappers.ProductMapper;
import com.springboot.store.repositories.CategoryRepository;
import com.springboot.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    private ProductMapper productMapper;


    @GetMapping(value = "/products")
    public List<ProductDto> getAllProduct(
            @RequestParam(required = false, defaultValue = "", name = "categoryId") Byte categoryId
    ){
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        }else{
//            products = productRepository.findAll();
            products = productRepository.findAllWIthCategory();
        }

        return products
                .stream()
                .map(product -> productMapper.toDto(product))
                .toList();
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        var productdto = productMapper.toDto(product);
        return ResponseEntity.ok(productdto);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<ProductDto> createProducts(
            @RequestBody RegisterProductRequest request,
            UriComponentsBuilder uribuilder
            )
    {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(request);

        product.setCategory(category);
        productRepository.save(product);

        ProductDto productdto = productMapper.toDto(product);

        var uri = uribuilder
                .path("/products/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(uri).body(productdto);
    }

    @PutMapping(value = "/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody RegisterProductRequest request,
            UriComponentsBuilder uribuilder
    ){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        productMapper.updateProductFromDto(request, product);

        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }

        product.setCategory(category);
        productRepository.save(product);

        ProductDto productdto = productMapper.toDto(product);

        var uri = uribuilder
                .path("/products/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(uri).body(productdto);
    }

    @DeleteMapping(value = "/products/{id}")
    public ResponseEntity<Void> Deleteproduct(@PathVariable Long id)
    {
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
