package com.example.KodnestSalesSavvy.adminserviceimplementations;

import com.example.KodnestSalesSavvy.adminservices.AdminBusinessServiceContract;
import com.example.KodnestSalesSavvy.entities.Order;
import com.example.KodnestSalesSavvy.entities.OrderItem;
import com.example.KodnestSalesSavvy.userrepositories.OrderItemRepository;
import com.example.KodnestSalesSavvy.userrepositories.OrderRepository;
import com.example.KodnestSalesSavvy.userrepositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminBusinessService implements AdminBusinessServiceContract {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public AdminBusinessService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Map<String, Object> calculateMonthlyBusiness(int month, int year) {
        return calculateBusinessMetrics(orderRepository.findSuccessfulOrdersByMonthAndYear(month, year));
    }

    @Override
    public Map<String, Object> calculateDailyBusiness(LocalDate date) {
        return calculateBusinessMetrics(orderRepository.findSuccessfulOrdersByDate(date));
    }

    @Override
    public Map<String, Object> calculateYearlyBusiness(int year) {
        return calculateBusinessMetrics(orderRepository.findSuccessfulOrdersByYear(year));
    }

    @Override
    public Map<String, Object> calculateOverallBusiness() {

        List<Order> successfulOrders =
                orderRepository.findAllSuccessfulOrders();

        return calculateBusinessMetrics(successfulOrders);
    }

    private Map<String, Object> calculateBusinessMetrics(List<Order> orders) {

        double totalRevenue = 0.0;
        Map<String, Integer> categorySales = new HashMap<>();

        if (orders == null || orders.isEmpty()) {
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("totalRevenue", 0.0);
            emptyResult.put("categorySales", categorySales);
            return emptyResult;
        }

        for (Order order : orders) {

            if (order == null) continue;

            if (order.getTotalAmount() != null) {
                totalRevenue += order.getTotalAmount().doubleValue();
            }
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getOrderId());
            if (items == null || items.isEmpty()) continue;
            for (OrderItem item : items) {
                if (item == null) continue;
                productRepository.findById(item.getProductId())
                        .ifPresent(product -> {
                            if (product.getCategory() != null && product.getCategory().getCategoryName() != null) {
                                String categoryName = product.getCategory().getCategoryName();
                                int quantity = item.getQuantity();

                                categorySales.put(categoryName,categorySales.getOrDefault(categoryName, 0) + quantity
                                );
                            }
                        });
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("categorySales", categorySales);
        return result;
    }
}