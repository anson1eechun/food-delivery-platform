package org.example.service;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 外送平台服務類別
 * 負責處理訂單流程、餐廳接單、外送員配送等功能
 */
public class DeliveryService {
    private static final Logger logger = LogManager.getLogger(DeliveryService.class);

    // 模擬資料庫儲存訂單
    private final Map<String, Order> orders = new HashMap<>();

    // 模擬餐廳狀態
    private final Map<String, Boolean> restaurantAvailability = new HashMap<>();

    // 模擬外送員狀態
    private final Map<String, Boolean> deliveryPersonAvailability = new HashMap<>();

    private final Random random = new Random();

    public DeliveryService() {
        // 初始化一些測試資料
        restaurantAvailability.put("R001", true);
        restaurantAvailability.put("R002", false); // 這家餐廳目前不營業
        restaurantAvailability.put("R003", true);

        deliveryPersonAvailability.put("D001", true);
        deliveryPersonAvailability.put("D002", true);
        deliveryPersonAvailability.put("D003", false);
    }

    /**
     * 顧客下單
     * @return 訂單物件
     */
    public Order createOrder(String customerId, String restaurantId, String foodItems,
                             double totalAmount, String deliveryAddress) {
        try {
            logger.info("顧客 {} 正在建立訂單，餐廳: {}, 品項: {}, 金額: {}",
                    customerId, restaurantId, foodItems, totalAmount);

            // 驗證訂單金額 (Unchecked Exception 可能發生的地方)
            if (totalAmount <= 0) {
                throw new IllegalArgumentException("訂單金額必須大於 0");
            }

            // 驗證地址不為空
            if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
                throw new IllegalArgumentException("外送地址不可為空");
            }

            String orderId = "ORD" + System.currentTimeMillis();
            Order order = new Order(orderId, customerId, restaurantId,
                    foodItems, totalAmount, deliveryAddress);

            orders.put(orderId, order);

            logger.info("訂單建立成功: {}", order);
            return order;

        } catch (IllegalArgumentException e) {
            // 處理 Unchecked Exception
            logger.error("建立訂單時發生參數錯誤: {}", e.getMessage(), e);
            throw e; // 重新拋出讓呼叫端處理
        } catch (Exception e) {
            // 捕捉其他意外錯誤
            logger.error("建立訂單時發生未預期的錯誤", e);
            throw new RuntimeException("系統錯誤，請稍後再試", e);
        }
    }

    /**
     * 餐廳接單
     * @throws RestaurantBusyException 餐廳忙碌或不可用
     * @throws OrderNotFoundException 訂單不存在
     * @throws InvalidOrderStatusException 訂單狀態不正確
     */
    public void acceptOrder(String orderId, String restaurantId)
            throws RestaurantBusyException, OrderNotFoundException, InvalidOrderStatusException {

        logger.info("餐廳 {} 嘗試接受訂單: {}", restaurantId, orderId);

        // 檢查訂單是否存在
        Order order = orders.get(orderId);
        if (order == null) {
            logger.error("訂單不存在: {}", orderId);
            throw new OrderNotFoundException(orderId);
        }

        // 檢查訂單狀態
        if (order.getStatus() != OrderStatus.PENDING) {
            logger.warn("訂單 {} 當前狀態為 {}，無法接單", orderId, order.getStatus());
            throw new InvalidOrderStatusException(orderId, order.getStatus(), OrderStatus.ACCEPTED);
        }

        // 檢查餐廳是否可用
        Boolean isAvailable = restaurantAvailability.get(restaurantId);
        if (isAvailable == null || !isAvailable) {
            logger.warn("餐廳 {} 目前無法接單（可能暫停營業或訂單已滿）", restaurantId);
            throw new RestaurantBusyException(restaurantId, "餐廳目前無法接單");
        }

        // 模擬隨機失敗情況（例如系統延遲、網路問題）
        if (random.nextInt(10) < 2) { // 20% 機率失敗
            logger.error("餐廳 {} 接單時發生系統錯誤", restaurantId);
            throw new RestaurantBusyException(restaurantId, "系統忙碌中，請稍後再試");
        }

        order.setStatus(OrderStatus.ACCEPTED);
        logger.info("餐廳 {} 成功接受訂單: {}", restaurantId, order);
    }

    /**
     * 餐廳準備餐點
     */
    public void prepareFood(String orderId) throws OrderNotFoundException, InvalidOrderStatusException {
        logger.info("開始準備訂單: {}", orderId);

        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new InvalidOrderStatusException(orderId, order.getStatus(), OrderStatus.PREPARING);
        }

        order.setStatus(OrderStatus.PREPARING);
        logger.info("訂單 {} 進入準備狀態", orderId);
    }

    /**
     * 餐點準備完成
     */
    public void foodReady(String orderId) throws OrderNotFoundException, InvalidOrderStatusException {
        logger.info("訂單餐點準備完成: {}", orderId);

        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new InvalidOrderStatusException(orderId, order.getStatus(), OrderStatus.READY);
        }

        order.setStatus(OrderStatus.READY);
        logger.info("訂單 {} 已完成準備，等待外送員取餐", orderId);
    }

    /**
     * 外送員接單
     * @throws DeliveryPersonUnavailableException 沒有可用的外送員
     */
    public void assignDeliveryPerson(String orderId, String deliveryPersonId)
            throws OrderNotFoundException, InvalidOrderStatusException, DeliveryPersonUnavailableException {

        logger.info("外送員 {} 嘗試接受訂單: {}", deliveryPersonId, orderId);

        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        if (order.getStatus() != OrderStatus.READY) {
            logger.warn("訂單 {} 尚未準備完成，無法派送", orderId);
            throw new InvalidOrderStatusException(orderId, order.getStatus(), OrderStatus.PICKED_UP);
        }

        // 檢查外送員是否可用
        Boolean isAvailable = deliveryPersonAvailability.get(deliveryPersonId);
        if (isAvailable == null || !isAvailable) {
            logger.warn("外送員 {} 目前不可用", deliveryPersonId);
            throw new DeliveryPersonUnavailableException(orderId, "外送員目前不可用");
        }

        order.setDeliveryPersonId(deliveryPersonId);
        order.setStatus(OrderStatus.PICKED_UP);
        deliveryPersonAvailability.put(deliveryPersonId, false); // 標記為忙碌

        logger.info("外送員 {} 已取餐，開始配送訂單: {}", deliveryPersonId, orderId);
    }

    /**
     * 完成配送
     */
    public void completeDelivery(String orderId) throws OrderNotFoundException, InvalidOrderStatusException {
        logger.info("嘗試完成訂單配送: {}", orderId);

        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        if (order.getStatus() != OrderStatus.PICKED_UP) {
            throw new InvalidOrderStatusException(orderId, order.getStatus(), OrderStatus.DELIVERED);
        }

        // 模擬配送過程中可能發生的 Unchecked Exception
        try {
            // 假設這裡有個計算配送時間的邏輯
            String address = order.getDeliveryAddress();
            int distance = address.length(); // 用地址長度模擬距離
            // 注意：這裡故意不除以 0，讓程式正常運行
            double deliveryTime = distance * 2.5; // 修正計算邏輯
            logger.debug("計算配送時間: {} 分鐘", deliveryTime);

        } catch (ArithmeticException e) {
            // 處理計算錯誤，但不影響配送完成
            logger.error("計算配送時間時發生錯誤，但配送已完成: {}", e.getMessage());
        }

        order.setStatus(OrderStatus.DELIVERED);

        // 釋放外送員
        if (order.getDeliveryPersonId() != null) {
            deliveryPersonAvailability.put(order.getDeliveryPersonId(), true);
        }

        logger.info("訂單 {} 配送完成！外送員: {}", orderId, order.getDeliveryPersonId());
    }

    /**
     * 取消訂單
     */
    public void cancelOrder(String orderId, String reason)
            throws OrderNotFoundException, InvalidOrderStatusException {

        logger.warn("收到訂單取消請求: {}, 原因: {}", orderId, reason);

        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // 已配送的訂單不能取消
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException(orderId, order.getStatus(), OrderStatus.CANCELLED);
        }

        // 如果外送員已接單，需要釋放外送員
        if (order.getStatus() == OrderStatus.PICKED_UP && order.getDeliveryPersonId() != null) {
            deliveryPersonAvailability.put(order.getDeliveryPersonId(), true);
            logger.info("已釋放外送員: {}", order.getDeliveryPersonId());
        }

        order.setStatus(OrderStatus.CANCELLED);
        logger.info("訂單 {} 已取消，原因: {}", orderId, reason);
    }

    /**
     * 查詢訂單
     */
    public Order getOrder(String orderId) throws OrderNotFoundException {
        Order order = orders.get(orderId);
        if (order == null) {
            logger.warn("查詢不存在的訂單: {}", orderId);
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }
}