package com.example.KodnestSalesSavvy.userserviceimplementations;

import com.example.KodnestSalesSavvy.entities.Category;
import com.example.KodnestSalesSavvy.entities.Product;
import com.example.KodnestSalesSavvy.entities.ProductImage;
import com.example.KodnestSalesSavvy.userrepositories.CategoryRepository;
import com.example.KodnestSalesSavvy.userrepositories.ProductImageRepositories;
import com.example.KodnestSalesSavvy.userrepositories.ProductRepository;
import com.example.KodnestSalesSavvy.userservices.ProductServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements ProductServiceContract {

    ProductRepository productRepository;

    ProductImageRepositories productImageRepositories;

    CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepositories productImageRepositories, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productImageRepositories = productImageRepositories;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                return productRepository.findByCategory_Id(category.getId());
            } else {
                throw new RuntimeException("Category not found");
            }
        } else {
            return productRepository.findAll();
        }
    }

    @Override
    public List<String> getProductImages(Integer productId) {
        List<ProductImage> productImages =
                productImageRepositories.findByProduct_Id(productId);


        List<String> imageUrls = new ArrayList<>();
        for (ProductImage image : productImages) {
            imageUrls.add(image.getImage_url());
        }
        return imageUrls;
    }
}
