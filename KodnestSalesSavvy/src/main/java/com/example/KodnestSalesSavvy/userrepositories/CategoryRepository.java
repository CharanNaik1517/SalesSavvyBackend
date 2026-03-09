package com.example.KodnestSalesSavvy.userrepositories;

import com.example.KodnestSalesSavvy.entities.Category;
import com.example.KodnestSalesSavvy.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Optional<Category> findByCategoryName(String categoryName);
}
