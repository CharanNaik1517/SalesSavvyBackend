package com.example.KodnestSalesSavvy.userservices;

import com.example.KodnestSalesSavvy.entities.User;

import java.util.Map;

public interface OrderServiceContract {
    public Map<String,Object> getOrdersForUser(User user);
}
