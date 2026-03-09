package com.example.KodnestSalesSavvy.userserviceimplementations;

import com.example.KodnestSalesSavvy.entities.Cart_Items;
import com.example.KodnestSalesSavvy.entities.Order;
import com.example.KodnestSalesSavvy.entities.OrderItem;
import com.example.KodnestSalesSavvy.entities.OrderStatus;
import com.example.KodnestSalesSavvy.userrepositories.CartRepository;
import com.example.KodnestSalesSavvy.userrepositories.OrderItemRepository;
import com.example.KodnestSalesSavvy.userrepositories.OrderRepository;
import com.example.KodnestSalesSavvy.userservices.PaymentServiceContract;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService implements PaymentServiceContract {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    public PaymentService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
    }


    @Override
    @Transactional
    public String createOrder(int userId, BigDecimal totalAmount, List<OrderItem> cartItems) throws RazorpayException {

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        var orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount.multiply(BigDecimal.valueOf(100)).intValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        Order order=new Order();
        order.setOrderId(razorpayOrder.get("id"));
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return razorpayOrder.get("id");
    }
    @Override
    @Transactional
    public boolean verifyPayment(String razorpayOrderId, String razorPaymentId, String razorpaySignature, int userId) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);


            if (isSignatureValid) {
                com.example.KodnestSalesSavvy.entities.Order order = orderRepository.findById(razorpayOrderId).orElseThrow();
                order.setStatus(OrderStatus.SUCCESS);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);

                List<Cart_Items> cartItems = cartRepository.findCartItemsWithProductDetails(userId);
                for (Cart_Items cartItem : cartItems) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProductId(cartItem.getProduct().getId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.getPricePerUnit();
                    orderItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    orderItemRepository.save(orderItem);

                }
                cartRepository.deleteAllCartItemsByUserId(userId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

