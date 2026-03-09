package com.example.KodnestSalesSavvy.adminservices;

import com.example.KodnestSalesSavvy.entities.Order;
import com.example.KodnestSalesSavvy.userrepositories.OrderItemRepository;
import com.example.KodnestSalesSavvy.userrepositories.OrderRepository;
import com.example.KodnestSalesSavvy.userrepositories.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface AdminBusinessServiceContract {

    public Map<String, Object> calculateMonthlyBusiness(int month,int year);
    public Map<String,Object> calculateDailyBusiness(LocalDate date);
    public Map<String,Object> calculateYearlyBusiness(int year);
    public Map<String,Object> calculateOverallBusiness();

    //public Map<String,Object> calculateBusinessMetrics(List<Order> orders);

    //public void AdminBusinessService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository);

}
