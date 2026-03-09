package com.example.KodnestSalesSavvy.usercontrollers;

import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userservices.CartServiceContract;
import com.example.KodnestSalesSavvy.userservices.UserServiceContract;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5174",allowCredentials = "true")
@RequestMapping("/api/cart")
public class CartController {
    CartServiceContract cartService;

    public CartController(CartServiceContract cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    @CrossOrigin(origins = "http://localhost:5174",allowCredentials = "true")
    public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request, HttpServletRequest req){

        User user= (User) req.getAttribute("authenticatedUser");
        String username=(String) request.get("username");
        int productId=(int) request.get("productId");

        int quantity=request.containsKey("quantity") ? (int) request.get("quantity") : 1;

        cartService.addToCart(user,productId,quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/items")
    public ResponseEntity<Map<String,Object>> getCartItems(HttpServletRequest request){
        User user= (User) request.getAttribute("authenticatedUser");

        Map<String,Object> response=cartService.getCartItems(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String,Object> request,HttpServletRequest req){
    String username=(String) request.get("username");
    int productId=(int) request.get("productId");
    int quantity=(int) request.get("quantity");

   User user= (User) req.getAttribute("authenticatedUser");
   cartService.updateCartItemQuantity(user,productId,quantity);
   return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String,Object> request,HttpServletRequest req){
    String username=(String) request.get("username");
    int productId=(int) request.get("productId");

    User user=(User) req.getAttribute("authenticatedUser");
    cartService.deleteCartItem(user.getUserId(),productId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/items/count")
    public ResponseEntity<Integer> getCartItemcount(@RequestParam String username,HttpServletRequest request){
        User user= (User) request.getAttribute("authenticatedUser");
        int cartCount=cartService.getCartItemCount(user.getUserId());
        return ResponseEntity.ok(cartCount);

    }
}
