package com.example.KodnestSalesSavvy.userservices;

import com.example.KodnestSalesSavvy.entities.User;

import java.util.Map;

public interface CartServiceContract {
    public void addToCart(User user, int productId, int quantity);
    public Map<String,Object> getCartItems(User authenticatedUser);
    public void updateCartItemQuantity(User authenticateduser,int productId,int quantity);
    public void deleteCartItem(int userid,int productId);
    public int getCartItemCount(int userId);

}
