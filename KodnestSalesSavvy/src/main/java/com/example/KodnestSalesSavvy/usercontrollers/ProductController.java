package com.example.KodnestSalesSavvy.usercontrollers;

import com.example.KodnestSalesSavvy.entities.Product;
import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userservices.ProductServiceContract;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceContract productService;
@Autowired
    public ProductController(ProductServiceContract productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(required = false) String category,
            HttpServletRequest request) {

        try {
            User authenticatedUser =
                    (User) request.getAttribute("authenticatedUser");

            if (authenticatedUser == null) {
                return ResponseEntity
                        .status(401)
                        .body(Map.of("error", "Unauthorized access"));
            }

            List<Product> products =
                    productService.getProductsByCategory(category);

            Map<String, Object> response = new HashMap<>();

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", authenticatedUser.getUsername());
            userInfo.put("role", authenticatedUser.getRole().name());
            response.put("user", userInfo);

            List<Map<String, Object>> productList = new ArrayList<>();

            for (Product product : products) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("product_id", product.getId());
                productDetails.put("name", product.getName());
                productDetails.put("description", product.getDescription());
                productDetails.put("price", product.getPrice());
                productDetails.put("stock", product.getStock());

                List<String> images =
                        productService.getProductImages(product.getId());

                productDetails.put("images", images);
                productList.add(productDetails);
            }

            response.put("products", productList);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
