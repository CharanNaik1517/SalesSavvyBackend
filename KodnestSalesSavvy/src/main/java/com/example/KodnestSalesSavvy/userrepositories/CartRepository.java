package com.example.KodnestSalesSavvy.userrepositories;

import com.example.KodnestSalesSavvy.entities.Cart_Items;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart_Items, Integer> {

    @Query("""
        SELECT c FROM Cart_Items c
        WHERE c.user.userId = :userId
        AND c.product.id = :productId
    """)
    Optional<Cart_Items> findByUserAndProduct(
            @Param("userId") int userId,
            @Param("productId") int productId
    );

    @Query("""
    SELECT c FROM Cart_Items c
    JOIN FETCH c.product p
    WHERE c.user.userId = :userId
""")
    List<Cart_Items> findCartItemsWithProductDetails(
            @Param("userId") int userId
    );

    @Query("UPDATE Cart_Items c SET c.quantity=:quantity WHERE c.id=:Cart_Items")
   void updateCartItemQuantity(int cartItemId,int quantity);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart_Items c WHERE c.user.userId=:userId AND c.product.id=:productId")
    void deleteCartItem(int userId,int productId);

    @Query("SELECT COALESCE(SUM(c.quantity),0)FROM Cart_Items c WHERE c.user.userId=:userId")
    int countTotalItems(int userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Cart_Items c WHERE c.user.userId=:userId")
    void deleteAllCartItemsByUserId(int userId);
}


