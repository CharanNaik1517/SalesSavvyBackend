package com.example.KodnestSalesSavvy.adminservices;

import com.example.KodnestSalesSavvy.entities.User;

public interface AdminUserServiceContract {
    public User modifyUser(Integer userId,String username,String email,String role);
    public User getUserById(Integer userId);
}
