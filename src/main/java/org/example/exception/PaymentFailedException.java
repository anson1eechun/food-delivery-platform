package org.example.exception;

/**
 * 付款失敗例外 (Checked Exception)
 */
public class PaymentFailedException extends Exception {
    private final String orderId;
    private final double amount;

    public PaymentFailedException(String orderId, double amount, String reason) {
        super(String.format("訂單 %s 付款失敗 (金額: %.2f): %s", orderId, amount, reason));
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }
}