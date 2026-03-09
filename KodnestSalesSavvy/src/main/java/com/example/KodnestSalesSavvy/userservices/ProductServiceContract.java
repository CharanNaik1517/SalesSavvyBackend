package com.example.KodnestSalesSavvy.userservices;

import com.example.KodnestSalesSavvy.entities.Product;

import java.util.List;

public interface ProductServiceContract {

    List<Product> getProductsByCategory(String categoryName);

    List<String> getProductImages(Integer productId);
}
