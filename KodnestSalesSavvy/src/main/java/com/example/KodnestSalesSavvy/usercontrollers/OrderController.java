package com.example.KodnestSalesSavvy.usercontrollers;

import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userserviceimplementations.OrderService;
import com.example.KodnestSalesSavvy.userservices.OrderServiceContract;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5174",allowCredentials = "true")
@RequestMapping("/api/orders")
public class OrderController {
    private OrderServiceContract orderService;

    public OrderController(OrderServiceContract orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrderForUser(HttpServletRequest request){
    try {
        User authenticatedUser=(User) request.getAttribute("authenticatedUser");
//        if (authenticatedUser==null){
//            return ResponseEntity.status(401).body(Map.of("error","User not authenticated"));
//        }
        Map<String,Object> response=orderService.getOrdersForUser(authenticatedUser);
        return ResponseEntity.ok(response);
    }
    catch (IllegalArgumentException e){
        return ResponseEntity.status(400).body(Map.of("error",e.getMessage()));
    }
    catch (Exception e){
        e.printStackTrace();
        return  ResponseEntity.status(500).body(Map.of("error","An unexpected error occurred"));
    }
    }
}
