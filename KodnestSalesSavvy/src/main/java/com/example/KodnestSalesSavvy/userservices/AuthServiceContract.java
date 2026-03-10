package com.example.KodnestSalesSavvy.userservices;

import com.example.KodnestSalesSavvy.entities.User;

public interface AuthServiceContract {
    public User authenticate(String username, String password);
    public String generateToken(User user);
    public String generateNewToken(User user);
    public boolean validateToken(String token);
    public String extractUsername(String token);
    public void logout(User user);

}
