# 🍔 美食外送平台 (Food Delivery Platform)

一個基於 Java 11 的美食外送平台後端系統，展示完整的訂單管理流程和例外處理機制。

## ✨ 功能特色

- 🛒 **完整訂單生命週期管理**：從下單到配送完成的完整流程
- 🏪 **餐廳管理**：支援餐廳接單、餐點準備等功能
- 🚚 **外送員管理**：外送員接單、配送狀態追蹤
- ⚠️ **健壯例外處理**：區分 Checked 和 Unchecked 例外
- 📊 **專業日誌系統**：使用 Log4j2 進行多層級日誌記錄
- 🧪 **完整測試情境**：7 個不同的業務場景測試

## 🏗️ 專案架構

```
food-delivery-platform/
├── src/main/
│   ├── java/org/example/           # Java 原始碼
│   │   ├── FoodDeliveryApp.java    # 主程式類別
│   │   ├── model/                  # 資料模型
│   │   │   ├── Order.java          # 訂單實體類別
│   │   │   └── OrderStatus.java    # 訂單狀態枚舉
│   │   ├── service/                # 業務服務
│   │   │   └── DeliveryService.java # 外送服務核心
│   │   └── exception/              # 自定義例外類別
│   │       ├── RestaurantBusyException.java
│   │       ├── OrderNotFoundException.java
│   │       ├── InvalidOrderStatusException.java
│   │       ├── DeliveryPersonUnavailableException.java
│   │       └── PaymentFailedException.java
│   ├── groovy/org/example/         # Groovy 檔案
│   │   └── Main.groovy             # Groovy 入口點
│   └── resources/
│       └── log4j2.xml              # 日誌配置檔
├── logs/                           # 日誌檔案目錄
├── lib/                            # 依賴 JAR 檔案
├── pom.xml                         # Maven 配置檔
└── README.md                       # 專案說明文件
```

## 🚀 快速開始

### 前置需求

- Java 11 或更高版本
- Maven 3.6+ (可選，也可使用 Java 直接編譯)

### 編譯與執行

#### 方法一：使用 Maven (推薦)

```bash
# 編譯專案
mvn clean compile

# 執行程式
mvn exec:java
```

#### 方法二：使用 Java 直接編譯

```bash
# 下載依賴 (如果尚未下載)
mkdir -p lib
curl -o lib/log4j-api-2.20.0.jar https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar
curl -o lib/log4j-core-2.20.0.jar https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-core/2.20.0/log4j-core-2.20.0.jar
curl -o lib/log4j-slf4j2-impl-2.20.0.jar https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-slf4j2-impl/2.20.0/log4j-slf4j2-impl-2.20.0.jar
curl -o lib/slf4j-api-2.0.7.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.7/slf4j-api-2.0.7.jar

# 編譯專案
mkdir -p target/classes
javac -cp src/main/java:target/classes:lib/* -d target/classes src/main/java/org/example/model/*.java
javac -cp src/main/java:target/classes:lib/* -d target/classes src/main/java/org/example/exception/*.java
javac -cp src/main/java:target/classes:lib/* -d target/classes src/main/java/org/example/service/*.java
javac -cp src/main/java:target/classes:lib/* -d target/classes src/main/java/org/example/FoodDeliveryApp.java

# 執行程式
java -cp "target/classes:lib/*:src/main/resources" org.example.FoodDeliveryApp
```

## 📋 測試情境

程式包含 7 個完整的測試情境：

1. **正常訂單流程** - 完整的訂單生命週期
2. **餐廳無法接單** - RestaurantBusyException 處理
3. **訂單參數錯誤** - IllegalArgumentException 處理
4. **訂單狀態轉換錯誤** - InvalidOrderStatusException 處理
5. **外送員不可用** - DeliveryPersonUnavailableException 處理
6. **訂單取消** - 取消流程處理
7. **查詢不存在訂單** - OrderNotFoundException 處理

## 📊 訂單狀態流程

```
PENDING (待確認) → ACCEPTED (餐廳已接單) → PREPARING (準備中) → READY (待取餐) → PICKED_UP (外送中) → DELIVERED (已送達)
                                                                    ↓
                                                              CANCELLED (已取消)
```

## 📝 日誌系統

專案使用 Log4j2 進行多層級日誌記錄：

- **app-error.log** - 錯誤和例外記錄
- **business.log** - 業務流程記錄
- **delivery-service.log** - 外送服務詳細記錄
- **控制台輸出** - 即時查看執行狀況

### 日誌級別

- **ERROR** - 錯誤和例外
- **WARN** - 警告訊息
- **INFO** - 一般資訊
- **DEBUG** - 詳細除錯資訊

## 🔧 例外處理機制

### Checked Exceptions (業務邏輯例外)

- `RestaurantBusyException` - 餐廳無法接單
- `OrderNotFoundException` - 訂單不存在
- `InvalidOrderStatusException` - 訂單狀態轉換錯誤
- `DeliveryPersonUnavailableException` - 外送員不可用
- `PaymentFailedException` - 付款失敗

### Unchecked Exceptions (參數驗證例外)

- `IllegalArgumentException` - 參數驗證錯誤

## 🛠️ 技術棧

- **Java 11** - 主要程式語言
- **Maven** - 專案管理和依賴管理
- **Log4j2** - 日誌框架
- **SLF4J** - 日誌介面

## 📦 依賴項目

```xml
<dependencies>
    <!-- Log4j 2 API -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.20.0</version>
    </dependency>
    
    <!-- Log4j 2 Core -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.20.0</version>
    </dependency>
    
    <!-- Log4j 2 SLF4J 實作 -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j2-impl</artifactId>
        <version>2.20.0</version>
    </dependency>
    
    <!-- SLF4J API -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.7</version>
    </dependency>
</dependencies>
```

## 🎯 執行結果範例

```
2025-10-06 07:29:11.543 [main] INFO  org.example.FoodDeliveryApp - ========== 美食外送平台啟動 ==========
2025-10-06 07:29:11.548 [main] INFO  org.example.service.DeliveryService - 顧客 C001 正在建立訂單，餐廳: R001, 品項: 珍珠奶茶x2, 雞排便當x1, 金額: 250.0
2025-10-06 07:29:11.551 [main] INFO  org.example.service.DeliveryService - 訂單建立成功: Order[id=ORD1759706951548, customer=C001, restaurant=R001, items=珍珠奶茶x2, 雞排便當x1, amount=250.00, status=待確認]
2025-10-06 07:29:14.571 [main] INFO  org.example.FoodDeliveryApp - 情境 1 完成：訂單 ORD1759706951548 成功配送
...
2025-10-06 07:29:14.590 [main] INFO  org.example.FoodDeliveryApp - ========== 所有測試情境執行完畢 ==========
```

## 🤝 貢獻指南

1. Fork 本專案
2. 創建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交變更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 開啟 Pull Request

## 📄 授權條款

本專案採用 MIT 授權條款 - 詳見 [LICENSE](LICENSE) 檔案

## 📞 聯絡資訊

如有問題或建議，請透過以下方式聯絡：

- 建立 [Issue](../../issues)
- 發送 Pull Request

---

⭐ 如果這個專案對您有幫助，請給我們一個 Star！
