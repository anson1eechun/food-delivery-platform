package org.example.exception;

/**
 * 餐廳忙碌例外 (Checked Exception)
 * 當餐廳訂單過多或暫停營業時拋出
 */
public class RestaurantBusyException extends Exception {
    private final String restaurantId;

    public RestaurantBusyException(String restaurantId, String message) {
        super(message);
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
}