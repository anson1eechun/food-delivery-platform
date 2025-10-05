package org.example.exception;

/**
 * 訂單找不到例外 (Checked Exception)
 */
public class OrderNotFoundException extends Exception {
    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super("找不到訂單: " + orderId);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}