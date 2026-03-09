package com.example.KodnestSalesSavvy.userserviceimplementations;

import com.example.KodnestSalesSavvy.entities.Cart_Items;
import com.example.KodnestSalesSavvy.entities.Product;
import com.example.KodnestSalesSavvy.entities.ProductImage;
import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userrepositories.CartRepository;
import com.example.KodnestSalesSavvy.userrepositories.ProductImageRepositories;
import com.example.KodnestSalesSavvy.userrepositories.ProductRepository;
import com.example.KodnestSalesSavvy.userrepositories.UserRepository;
import com.example.KodnestSalesSavvy.userservices.CartServiceContract;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService implements CartServiceContract {

    ProductRepository productRepository;
    CartRepository cartRepository;
    ProductImageRepositories productImageRepositories;
    UserRepository userRepository;

    public CartService(ProductRepository productRepository, CartRepository cartRepository, ProductImageRepositories productImageRepositories, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.productImageRepositories = productImageRepositories;
        this.userRepository = userRepository;
    }

    @Override
    public void addToCart(User user, int productId, int quantity) {
        Product product= productRepository.findById(productId).
                orElseThrow(()-> new IllegalArgumentException("Product not found with ID:+productid"));
        Optional<Cart_Items> existingItem=cartRepository.findByUserAndProduct(user.getUserId(),productId);

        if(existingItem.isPresent()){
            Cart_Items cartItem=existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepository.save(cartItem);
        }
        else {
            Cart_Items newItem=new Cart_Items(user,product,quantity);
            cartRepository.save(newItem);
        }
    }

    @Override
    public Map<String, Object> getCartItems(User authenticatedUser) {
        List<Cart_Items> cartItems=cartRepository.findCartItemsWithProductDetails(authenticatedUser.getUserId());

        Map<String,Object> response=new HashMap();

        response.put("username",authenticatedUser.getUsername());
        response.put("role",authenticatedUser.getRole().toString());

        List<Map<String,Object>> products=new ArrayList<>();
        int overallTotalPrice=0;

        for (Cart_Items cartItem:cartItems){
            Map<String,Object> productDetails=new HashMap<>();
            Product product=cartItem.getProduct();
            List<ProductImage> productImages=productImageRepositories.findByProduct_Id(product.getId());
            String imageUrl=(productImages != null && !productImages.isEmpty()) ? productImages.get(0).getImage_url():"default-image-url";
            productDetails.put("product_id",product.getId());
            productDetails.put("image_url",imageUrl);
            productDetails.put("name",product.getName());
            productDetails.put("description",product.getDescription());
            productDetails.put("price_per_unit",product.getPrice());
            productDetails.put("quantity",cartItem.getQuantity());
            productDetails.put("total_price",cartItem.getQuantity()*product.getPrice().doubleValue());

            products.add(productDetails);
            overallTotalPrice+=cartItem.getQuantity()*product.getPrice().doubleValue();
        }
        Map<String,Object> cart=new HashMap<>();
        cart.put("products",products);
        cart.put("overall_total_price",overallTotalPrice);

        response.put("cart",cart);

        return response;

    }

    @Override
    public void updateCartItemQuantity(User authenticateduser, int productId, int quantity) {

    User ref=userRepository.findById(authenticateduser.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

    Product product=productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product not found"));

    Optional<Cart_Items> existingItem=cartRepository.findByUserAndProduct(authenticateduser.getUserId(),productId);

    if(existingItem.isPresent()){
        Cart_Items item=existingItem.get();
        if(quantity==0){
            deleteCartItem(authenticateduser.getUserId(),productId);
        }
        else {
            item.setQuantity(quantity);
            cartRepository.save(item);
        }
    }
    else {
        throw new RuntimeException("Cart Item not found associated with product and user");
    }
    }
    public void deleteCartItem(int userid,int productId){
        Product product=productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product not found"));
        cartRepository.deleteCartItem(userid,productId);

    }

    @Override
    public int getCartItemCount(int userId) {
        return cartRepository.countTotalItems(userId);
    }
}
