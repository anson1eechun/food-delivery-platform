package org.example;

import org.example.model.Order;
import org.example.service.DeliveryService;
import org.example.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 外送平台主程式
 * 模擬各種訂單流程和例外情況
 */
public class FoodDeliveryApp {
    private static final Logger logger = LogManager.getLogger(FoodDeliveryApp.class);

    public static void main(String[] args) {
        logger.info("========== 美食外送平台啟動 ==========");

        DeliveryService service = new DeliveryService();

        // 情境 1: 正常訂單流程
        testNormalOrderFlow(service);

        // 情境 2: 餐廳無法接單
        testRestaurantBusy(service);

        // 情境 3: 訂單參數錯誤 (Unchecked Exception)
        testInvalidOrderParameters(service);

        // 情境 4: 訂單狀態轉換錯誤
        testInvalidStatusTransition(service);

        // 情境 5: 外送員不可用
        testDeliveryPersonUnavailable(service);

        // 情境 6: 訂單取消
        testOrderCancellation(service);

        // 情境 7: 查詢不存在的訂單
        testOrderNotFound(service);

        logger.info("========== 所有測試情境執行完畢 ==========");
    }

    /**
     * 情境 1: 正常的訂單流程
     */
    private static void testNormalOrderFlow(DeliveryService service) {
        logger.info("\n--- 情境 1: 正常訂單流程 ---");

        try {
            // 顧客下單
            Order order = service.createOrder(
                    "C001",
                    "R001",
                    "珍珠奶茶x2, 雞排便當x1",
                    250.0,
                    "台中市西屯區文華路100號"
            );

            String orderId = order.getOrderId();

            // 餐廳接單
            service.acceptOrder(orderId, "R001");
            Thread.sleep(500); // 模擬時間流逝

            // 準備餐點
            service.prepareFood(orderId);
            Thread.sleep(1000); // 模擬準備時間

            // 餐點完成
            service.foodReady(orderId);

            // 外送員接單
            service.assignDeliveryPerson(orderId, "D001");
            Thread.sleep(1500); // 模擬配送時間

            // 完成配送
            service.completeDelivery(orderId);

            logger.info("情境 1 完成：訂單 {} 成功配送", orderId);

        } catch (Exception e) {
            logger.error("情境 1 執行時發生錯誤", e);
        }
    }

    /**
     * 情境 2: 餐廳無法接單 (Checked Exception)
     */
    private static void testRestaurantBusy(DeliveryService service) {
        logger.info("\n--- 情境 2: 餐廳無法接單 ---");

        try {
            Order order = service.createOrder(
                    "C002",
                    "R002",  // 這家餐廳目前不營業
                    "牛肉麵x1",
                    120.0,
                    "台中市北區三民路50號"
            );

            // 嘗試讓餐廳接單
            service.acceptOrder(order.getOrderId(), "R002");

        } catch (RestaurantBusyException e) {
            // 正確處理業務邏輯例外
            logger.warn("餐廳 {} 無法接單: {}", e.getRestaurantId(), e.getMessage());
            logger.info("系統將自動尋找其他餐廳...");
        } catch (Exception e) {
            logger.error("情境 2 執行時發生未預期的錯誤", e);
        }
    }

    /**
     * 情境 3: 訂單參數錯誤 (Unchecked Exception)
     */
    private static void testInvalidOrderParameters(DeliveryService service) {
        logger.info("\n--- 情境 3: 訂單參數錯誤 ---");

        try {
            // 嘗試建立金額為負數的訂單
            service.createOrder(
                    "C003",
                    "R001",
                    "炸雞套餐",
                    -50.0,  // 錯誤的金額
                    "台中市南區建國路200號"
            );
        } catch (IllegalArgumentException e) {
            // 處理參數驗證錯誤
            logger.error("訂單建立失敗：參數驗證錯誤 - {}", e.getMessage());
        }

        try {
            // 嘗試建立空地址的訂單
            service.createOrder(
                    "C004",
                    "R001",
                    "披薩",
                    300.0,
                    ""  // 空地址
            );
        } catch (IllegalArgumentException e) {
            logger.error("訂單建立失敗：{}", e.getMessage());
        }
    }

    /**
     * 情境 4: 訂單狀態轉換錯誤 (Checked Exception)
     */
    private static void testInvalidStatusTransition(DeliveryService service) {
        logger.info("\n--- 情境 4: 訂單狀態轉換錯誤 ---");

        try {
            Order order = service.createOrder(
                    "C005",
                    "R001",
                    "壽司組合",
                    450.0,
                    "台中市東區自由路88號"
            );

            // 嘗試在未接單的情況下直接準備餐點
            service.prepareFood(order.getOrderId());

        } catch (InvalidOrderStatusException e) {
            logger.warn("狀態轉換錯誤: {}", e.getMessage());
            logger.info("請確保依照正確的流程執行操作");
        } catch (Exception e) {
            logger.error("情境 4 執行時發生錯誤", e);
        }
    }

    /**
     * 情境 5: 外送員不可用 (Checked Exception)
     */
    private static void testDeliveryPersonUnavailable(DeliveryService service) {
        logger.info("\n--- 情境 5: 外送員不可用 ---");

        try {
            Order order = service.createOrder(
                    "C006",
                    "R003",
                    "火鍋套餐",
                    680.0,
                    "台中市西區公益路123號"
            );

            String orderId = order.getOrderId();

            // 完成餐廳準備流程
            service.acceptOrder(orderId, "R003");
            service.prepareFood(orderId);
            service.foodReady(orderId);

            // 嘗試指派不可用的外送員
            service.assignDeliveryPerson(orderId, "D003");

        } catch (DeliveryPersonUnavailableException e) {
            logger.warn("外送員不可用: {}", e.getMessage());
            logger.info("系統將自動尋找其他外送員...");
        } catch (Exception e) {
            logger.error("情境 5 執行時發生錯誤", e);
        }
    }

    /**
     * 情境 6: 訂單取消
     */
    private static void testOrderCancellation(DeliveryService service) {
        logger.info("\n--- 情境 6: 訂單取消 ---");

        try {
            Order order = service.createOrder(
                    "C007",
                    "R001",
                    "漢堡套餐",
                    180.0,
                    "台中市北屯區文心路456號"
            );

            service.acceptOrder(order.getOrderId(), "R001");

            // 顧客決定取消訂單
            service.cancelOrder(order.getOrderId(), "顧客臨時有事，改為自取");

        } catch (Exception e) {
            logger.error("情境 6 執行時發生錯誤", e);
        }
    }

    /**
     * 情境 7: 查詢不存在的訂單 (Checked Exception)
     */
    private static void testOrderNotFound(DeliveryService service) {
        logger.info("\n--- 情境 7: 查詢不存在的訂單 ---");

        try {
            // 嘗試查詢一個不存在的訂單
            service.getOrder("ORD_NOT_EXISTS");

        } catch (OrderNotFoundException e) {
            logger.warn("查詢失敗: {}", e.getMessage());
        }
    }
}