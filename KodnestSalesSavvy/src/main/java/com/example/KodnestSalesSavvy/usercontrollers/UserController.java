package com.example.KodnestSalesSavvy.usercontrollers;

import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.entities.userdao;
import com.example.KodnestSalesSavvy.userservices.UserServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public  final UserServiceContract userService;

    @Autowired
    public UserController(UserServiceContract userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try{
            User registeredUser=userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message","User registered successfully","user",new userdao(registeredUser.getUserId(),registeredUser.getUsername(),registeredUser.getEmail(),registeredUser.getRole().toString())));
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
}
