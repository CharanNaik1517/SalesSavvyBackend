package com.example.KodnestSalesSavvy.userrepositories;

import com.example.KodnestSalesSavvy.entities.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepositories extends JpaRepository<ProductImage, Integer> {

    // ✅ FIXED: Product → id
    List<ProductImage> findByProduct_Id(Integer id);

    // ✅ FIXED: lowercase id (Java field name)
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImage pi WHERE pi.product.id = :id")
    void deleteByProductId(@Param("id") Integer id);
}
