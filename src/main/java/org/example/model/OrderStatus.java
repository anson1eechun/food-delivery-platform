package org.example.model;

/**
 * 訂單狀態枚舉
 */
public enum OrderStatus {
    PENDING("待確認"),           // 顧客已下單，等待餐廳確認
    ACCEPTED("餐廳已接單"),      // 餐廳已接受訂單
    PREPARING("準備中"),         // 餐廳正在準備餐點
    READY("待取餐"),            // 餐點已完成，等待外送員取餐
    PICKED_UP("外送中"),        // 外送員已取餐，正在配送
    DELIVERED("已送達"),        // 訂單已完成配送
    CANCELLED("已取消");        // 訂單已取消

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}