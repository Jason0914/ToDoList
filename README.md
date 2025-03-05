# Todo 專案技術報告

## 1. 功能需求與流程

### 待辦事項管理功能

- 新增待辦事項
- 刪除待辦事項
- 暫時隱藏待辦事項

### 用戶功能

- 用戶註冊
- 用戶登入
- 忘記密碼

### 記帳功能實現流程

- 新增交易記錄
- 更新交易記錄
- 刪除交易記錄
- 查詢交易列表
- 按條件篩選交易
- 計算財務摘要
- 交易類別管理
- 交易日期與金額追蹤
- 備註信息添加

## 2. 技術架構

- **後端**：Java、Spring Boot
- **前端**：React、Bootstrap
- **數據庫**：MySQL

**部署配置**

- 前端部署端口：5173
- 後端 API 端口：8080
- 本地開發 URL：[http://localhost:5173](http://localhost:5173/) (前端)、[http://localhost:8080](http://localhost:8080/) (後端)

**後端開發分層架構：**

1. 創建實體類 (Entity) - 映射數據庫結構
2. 創建 DTO - 定義數據傳輸格式
3. 實現 Repository - 處理數據訪問
4. 定義 Service - 規範業務邏輯
5. 實現 Service - 實現業務邏輯
6. 創建 Controller - 處理 HTTP 請求
7. 添加異常處理 - 提供友好的錯誤響應
8. 配置安全性和其他功能

### **前端目錄結構**

```json
src/
├── App.jsx               // 主應用組件和路由配置
├── main.jsx              // 應用入口點
├── App.css               // 全局樣式
├── styles/
│   └── custom.css        // 自定義樣式
├── pages/                // 頁面組件
│   ├── Home.jsx          // 首頁
│   ├── Login.jsx         // 登入頁
│   ├── Register.jsx      // 註冊頁
│   ├── TodoPage.jsx      // 待辦事項頁面
│   ├── TransactionPage.jsx // 記帳頁面
│   ├── ForgotPassword.jsx  // 忘記密碼頁面
│   └── ResetPassword.jsx   // 重設密碼頁面
├── components/           // 通用組件
│   ├── ProtectedRoute.jsx  // 受保護路由組件
│   ├── TodoList.jsx      // 待辦事項列表
│   ├── TodoItem.jsx      // 待辦事項項目
│   ├── TodoInput.jsx     // 待辦事項輸入
│   └── transactions/     // 交易相關組件
│       ├── TransactionList.jsx    // 交易列表
│       ├── TransactionForm.jsx    // 交易表單
│       ├── TransactionChart.jsx   // 交易圖表
│       └── TransactionSummary.jsx // 交易摘要
├── contexts/
│   └── AuthContext.jsx   // 認證上下文
└── services/             // API 服務
├── userService.js    // 用戶服務 (登入/註冊)
├── todoService.js    // 待辦事項服務
└── transactionService.js // 交易服務
```

### API結構

```json
/api/
├── users/
│   ├── /register        // 用戶註冊
│   ├── /login           // 用戶登入
│   ├── /logout          // 用戶登出
│   ├── /{username}      // 獲取用戶資訊
│   ├── /exists/username/{username} // 檢查用戶名是否存在
│   ├── /exists/email/{email}       // 檢查信箱是否存在
│   └── /password-reset/  // 密碼重設相關 API
├── todolist/
│   ├── /                // 獲取所有待辦事項 (GET) 或新增 (POST)
│   └── /{id}            // 更新 (PUT) 或刪除 (DELETE) 待辦事項
└── transactions/
├── /                // 獲取所有交易 (GET) 或新增 (POST)
├── /{id}            // 更新 (PUT) 或刪除 (DELETE) 交易
├── /range          // 獲取特定時間範圍的交易
├── /category/{category} // 獲取特定類別的交易
└── /summary        // 獲取交易統計信息
```

## 3.資料庫關聯

- **todo → users**
    - 一對多關係：一個用戶可以有多個待辦事項
- **transactions → users**
    - 一對多關係：一個用戶可以有多個交易記錄
- **password_reset_token → users**
    - 一對一關係：一個用戶對應一個密碼重設令牌

![資料庫關聯圖](/src/main/resources/static/images/database_diagram.png)

## 4. 重點細節部分

### 4.1 忘記密碼的郵件實現

### 技術實現思路

**1.令牌生成與存儲**

```java
// 生成安全的隨機令牌
String token = UUID.randomUUID().toString();

// 創建並保存密碼重設令牌
PasswordResetToken resetToken = new PasswordResetToken();
resetToken.setUser(user);
resetToken.setToken(token);
resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));  // 24小時有效期
tokenRepository.save(resetToken);
```

**2.環境設置**

```java
# application.properties 配置
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 安全性考量

1. **令牌唯一性**
2. **過期機制**

### 4.2 安全性保護使用者密碼加密

### 加密策略與實現

**BCrypt加密實現**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// 在用戶註冊時加密密碼
user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

// 在用戶登入時驗證密碼
if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
    throw new RuntimeException("密碼錯誤");
}
```

### 安全策略

- **使用BCrypt算法**：自動處理鹽值和多輪雜湊，抵抗彩虹表攻擊

**為何這邊不使用SHA-256而是BCrypt**

SHA-256本質上是一種通用雜湊算法，是用來處理快速計算，這在密碼安全領域反而成為缺點。攻擊者可以在短時間內嘗試大量可能的密碼組合。即使加入鹽值，SHA-256的計算速度仍然太快，無法有效抵禦暴力破解。
結論就是，BCrypt通過刻意減慢加密速度，提供了更強的抗暴力破解。

### 4.3 響應式設計實現

**手機、平板都能方便使用**

```css
/* 基本樣式 */
.transaction-card {
  padding: 15px;
  margin-bottom: 15px;
}

/* 平板設備 */
@media (max-width: 768px) {
  .transaction-card {
    padding: 10px;
  }
}

/* 手機設備 */
@media (max-width: 576px) {
  .transaction-card {
    padding: 8px;
    margin-bottom: 10px;
  }
}
```

## **5.畫面展示**

### 首頁

![首頁截圖](/src/main/resources/static/images/home_page.png)

### 登入

![登入頁面](/src/main/resources/static/images/login_page.png)

### 註冊帳號

![螢幕擷取畫面 2025-03-04 001445.png](%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2_2025-03-04_001445.png)

### 登入後進入待辦事項

![螢幕擷取畫面 2025-03-04 001540.png](%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2_2025-03-04_001540.png)

### 暫時隱藏待辦事項

![螢幕擷取畫面 2025-03-04 001554.png](%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2_2025-03-04_001554.png)

### 記帳功能

![螢幕擷取畫面 2025-03-04 001611.png](%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2_2025-03-04_001611.png)

![螢幕擷取畫面 2025-03-04 001625.png](%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2_2025-03-04_001625.png)
