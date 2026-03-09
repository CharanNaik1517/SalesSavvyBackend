package com.example.KodnestSalesSavvy.adminserviceimplementations;

import com.example.KodnestSalesSavvy.adminservices.AdminProductServiceContract;
import com.example.KodnestSalesSavvy.entities.Category;
import com.example.KodnestSalesSavvy.entities.Product;
import com.example.KodnestSalesSavvy.entities.ProductImage;
import com.example.KodnestSalesSavvy.userrepositories.CategoryRepository;
import com.example.KodnestSalesSavvy.userrepositories.ProductImageRepositories;
import com.example.KodnestSalesSavvy.userrepositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminProductService implements AdminProductServiceContract {

    private ProductRepository productRepository;
    private ProductImageRepositories imageRepositories;
    private CategoryRepository categoryRepository;

    public AdminProductService(ProductRepository productRepository, ProductImageRepositories imageRepositories, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.imageRepositories = imageRepositories;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl) {

        Optional<Category> category=categoryRepository.findById(categoryId);
        if(category.isEmpty()){
            throw new IllegalArgumentException("Invalid category ID");
        }
        Product product=new Product(name,description, BigDecimal.valueOf(price),stock,category.get(), LocalDateTime.now(),LocalDateTime.now());
        Product savedProduct=productRepository.save(product);
        if(imageUrl!=null && !imageUrl.isEmpty()){
            ProductImage image=new ProductImage(imageUrl, savedProduct);
            imageRepositories.save(image);
        }
        return savedProduct;
    }

    @Override
    public void deleteProduct(Integer productId) {
            if (!productRepository.existsById(productId)) {
                throw new IllegalArgumentException("Product not found");
            }
            imageRepositories.deleteByProductId(productId);
            productRepository.deleteById(productId);

        }
}



