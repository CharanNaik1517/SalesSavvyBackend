package com.example.KodnestSalesSavvy.adminservices;

import com.example.KodnestSalesSavvy.entities.Product;


import java.util.Map;

public interface AdminProductServiceContract {
    public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl);
    public void deleteProduct(Integer productId);
}
