package org.example.exception;

/**
 * 外送員不可用例外 (Checked Exception)
 */
public class DeliveryPersonUnavailableException extends Exception {
    private final String orderId;

    public DeliveryPersonUnavailableException(String orderId, String message) {
        super(message);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}