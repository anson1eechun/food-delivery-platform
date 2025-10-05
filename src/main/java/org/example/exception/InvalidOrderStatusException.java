package org.example.exception;

import org.example.model.OrderStatus;

/**
 * 訂單狀態無效例外 (Checked Exception)
 * 當訂單狀態轉換不合法時拋出
 */
public class InvalidOrderStatusException extends Exception {
    private final String orderId;
    private final OrderStatus currentStatus;
    private final OrderStatus attemptedStatus;

    public InvalidOrderStatusException(String orderId, OrderStatus currentStatus,
                                       OrderStatus attemptedStatus) {
        super(String.format("訂單 %s 無法從 %s 轉換到 %s",
                orderId, currentStatus.getDescription(), attemptedStatus.getDescription()));
        this.orderId = orderId;
        this.currentStatus = currentStatus;
        this.attemptedStatus = attemptedStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public OrderStatus getAttemptedStatus() {
        return attemptedStatus;
    }
}