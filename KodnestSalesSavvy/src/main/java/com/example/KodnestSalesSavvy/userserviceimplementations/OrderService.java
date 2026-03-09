package com.example.KodnestSalesSavvy.userserviceimplementations;

import com.example.KodnestSalesSavvy.entities.OrderItem;
import com.example.KodnestSalesSavvy.entities.Product;
import com.example.KodnestSalesSavvy.entities.ProductImage;
import com.example.KodnestSalesSavvy.entities.User;
import com.example.KodnestSalesSavvy.userrepositories.OrderItemRepository;
import com.example.KodnestSalesSavvy.userrepositories.ProductImageRepositories;
import com.example.KodnestSalesSavvy.userrepositories.ProductRepository;
import com.example.KodnestSalesSavvy.userservices.OrderServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService implements OrderServiceContract {

    private OrderItemRepository orderItemRepository;

    private ProductRepository productRepository;

    private ProductImageRepositories productImageRepositories;

    public OrderService(OrderItemRepository orderItemRepository, ProductRepository productRepository, ProductImageRepositories productImageRepositories) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.productImageRepositories = productImageRepositories;
    }

    @Override
    public Map<String, Object> getOrdersForUser(User user) {
        List<OrderItem> orderItems=orderItemRepository.findSuccessfulOrderItemsByUserId(user.getUserId());

        Map<String,Object> response=new HashMap<>();
        response.put("username",user.getUsername());
        response.put("role",user.getRole());

        List<Map<String,Object>> products=new ArrayList<>();

        for(OrderItem item:orderItems){
            Product product=productRepository.findById(item.getProductId()).orElse(null);
            if(product==null){
                continue;
            }
            List<ProductImage> images=productImageRepositories.findByProduct_Id(product.getId());
            String imageUrl=images.isEmpty()?null:images.get(0).getImage_url();

            Map<String,Object> productDetails=new HashMap<>();
            productDetails.put("order_id",item.getOrder().getOrderId());
            productDetails.put("quantity",item.getQuantity());
            productDetails.put("total_price",item.getTotalPrice());
            productDetails.put("image_url",imageUrl);
            productDetails.put("product_id",product.getId());
            productDetails.put("name",product.getName());
            productDetails.put("description",product.getDescription());
            productDetails.put("price_per_unit",item.getPricePerUnit());
            products.add(productDetails);
        }
        response.put("products",products);
        return response;
    }
}
