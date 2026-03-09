package com.example.KodnestSalesSavvy.userservices;

import com.example.KodnestSalesSavvy.entities.OrderItem;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentServiceContract {
    public String createOrder(int userId, BigDecimal totalAmount, List<OrderItem> cartItems) throws RazorpayException;
    public boolean verifyPayment(String razorpayOrderId,String razorPaymentId,String razorpaySignature,int userId);
}
