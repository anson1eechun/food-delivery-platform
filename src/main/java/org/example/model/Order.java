package org.example.model;

import java.time.LocalDateTime;

/**
 * 訂單類別
 */
public class Order {
    private final String orderId;
    private final String customerId;
    private final String restaurantId;
    private String deliveryPersonId;
    private final String foodItems;
    private final double totalAmount;
    private OrderStatus status;
    private final LocalDateTime orderTime;
    private final String deliveryAddress;

    public Order(String orderId, String customerId, String restaurantId,
                 String foodItems, double totalAmount, String deliveryAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.foodItems = foodItems;
        this.totalAmount = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public String getFoodItems() {
        return foodItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Setters
    public void setDeliveryPersonId(String deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%s, customer=%s, restaurant=%s, items=%s, amount=%.2f, status=%s]",
                orderId, customerId, restaurantId, foodItems, totalAmount, status.getDescription());
    }
}