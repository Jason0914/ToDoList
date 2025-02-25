# Todo & Transaction Management System - 專案架構文檔

## 專案概述
這個專案是一個多功能個人管理系統，包含待辦事項管理與記帳功能，採用前後端分離架構設計。系統提供了使用者註冊、登入、待辦事項管理、收支記錄與統計分析等功能。

## 系統架構

### 前後端分離設計
- **前端**：React.js 實現的單頁應用程式 (SPA)
- **後端**：Spring Boot RESTful API
- **通訊**：HTTP/JSON 格式
- **認證**：Session-based 認證機制

## 技術棧

### 前端技術
- **框架**：React.js
- **路由**：React Router DOM
- **狀態管理**：React Context API
- **UI 框架**：Bootstrap 5
- **圖表可視化**：Recharts
- **HTTP 請求**：Fetch API
- **樣式**：Custom CSS 與 CSS 動畫

### 後端技術
- **框架**：Spring Boot
- **安全性**：Spring Security
- **ORM**：Spring Data JPA
- **依賴注入**：Spring IoC 容器
- **日誌管理**：SLF4J + Logback
- **密碼加密**：BCrypt
- **格式轉換**：ModelMapper (Entity 與 DTO 轉換)
- **郵件服務**：JavaMail API
- **代碼簡化**：Lombok

### 數據庫
- 關聯式數據庫 (根據 JPA 配置推測)
- 表結構：
  - users (用戶表)
  - todo (待辦事項表)
  - transactions (交易記錄表)
  - password_reset_token (密碼重設令牌表)

## 應用架構

### 前端架構

#### 目錄結構
```
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

#### 組件架構
- **App.jsx**：應用程式根組件，配置路由與導航
- **認證模塊**：登入、註冊、忘記密碼流程
- **待辦事項模塊**：待辦事項的 CRUD 操作
- **交易記錄模塊**：交易記錄的 CRUD 操作與圖表分析

#### 狀態管理
- AuthContext：管理用戶認證狀態，提供登入/登出功能

### 後端架構

#### 分層設計
1. **Controller 層**：處理 HTTP 請求/響應，轉發給 Service 層
2. **Service 層**：業務邏輯處理，調用 Repository 層
3. **Repository 層**：數據訪問層，與數據庫交互
4. **Model 層**：
   - Entity：數據庫映射實體
   - DTO：數據傳輸對象

#### API 結構
```
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

## 核心功能實現

### 用戶認證與授權
- 基於 Session 的認證機制
- 密碼使用 BCrypt 加密存儲
- ProtectedRoute 組件實現前端路由保護
- 使用 Spring Security 實現後端安全控制
- 密碼重設功能，通過郵件發送重設連結

### 待辦事項管理
- 待辦事項的新增、查詢、更新、刪除 (CRUD)
- 待辦事項與用戶關聯，確保數據隔離
- 標記完成/未完成功能

### 交易記錄管理
- 收入與支出記錄的新增、查詢、更新、刪除
- 按日期範圍和類別篩選交易
- 交易統計分析：總收入、總支出、結餘
- 數據可視化：收支分析和支出分布圖表

## 設計模式與最佳實踐

### 設計模式
1. **MVC 模式**：前後端分離各自實現 MVC
2. **分層架構**：後端採用 Controller-Service-Repository 分層
3. **依賴注入**：使用 Spring 的 DI 實現松耦合
4. **數據訪問對象**：DTO 模式用於數據傳輸
5. **單例模式**：Spring Bean 默認為單例

### 安全性實現
- CORS 配置確保跨域安全
- Session-based 認證
- 密碼加密存儲
- 安全的密碼重設流程

### 響應式設計
- 使用 Bootstrap 實現響應式 UI
- 移動端友好的界面設計

### 異常處理
- 全局異常處理器 (GlobalExceptionHandler)
- 自定義業務異常 (如 TodoNotFoundException)
- 標準化 API 響應格式 (ApiResponse)

## 數據模型

### 用戶 (User)
- id：主鍵
- username：用戶名（唯一）
- password：密碼（加密存儲）
- email：電子郵件（唯一）
- createTime：創建時間
- updateTime：更新時間

### 待辦事項 (Todo)
- id：主鍵
- text：待辦事項內容
- completed：完成狀態
- user_id：關聯用戶 ID (外鍵)

### 交易記錄 (Transaction)
- id：主鍵
- date：交易日期
- type：交易類型 (INCOME/EXPENSE)
- amount：金額
- category：類別
- note：備註
- user_id：關聯用戶 ID (外鍵)

### 密碼重設令牌 (PasswordResetToken)
- id：主鍵
- token：重設令牌
- user_id：關聯用戶 ID (外鍵)
- expiryDate：過期時間

## 開發與部署

### 開發環境
- 前端：
  - Node.js 環境
  - Vite 作為構建工具
  - React 開發環境
- 後端：
  - Java JDK
  - Spring Boot 開發環境
  - Maven 構建工具

### 部署配置
- 前端部署端口：5173
- 後端 API 端口：8080
- 本地開發 URL：http://localhost:5173 (前端)、http://localhost:8080 (後端)

## 特色與優點

1. **完整的前後端分離架構**：清晰的責任分離，便於獨立開發與擴展
2. **豐富的用戶體驗**：動畫效果、漸變色彩、響應式設計
3. **安全的認證機制**：加密密碼、安全的密碼重設流程
4. **數據可視化**：圖表展示收支情況，直觀易理解
5. **多語言支持**：界面設計支持中文
6. **優雅的錯誤處理**：用戶友好的錯誤提示
7. **模塊化設計**：便於維護與擴展

## 未來擴展方向

1. **API 文檔集成**：添加 Swagger 或 OpenAPI 文檔
2. **單元測試覆蓋**：提高代碼質量與穩定性
3. **多主題支持**：深色模式支持
4. **多語言國際化**：支持更多語言
5. **推送通知**：待辦事項到期提醒
6. **數據導入/導出**：CSV/Excel 數據導入導出功能
7. **更豐富的數據分析**：支出趨勢、預算計劃等

## 結論

本專案實現了一個功能完整的個人管理系統，涵蓋待辦事項管理與財務記錄功能。通過採用現代化的前後端分離架構與主流技術棧，系統具有良好的可維護性、擴展性與用戶體驗。專案中實現的設計模式與最佳實踐確保了代碼質量與系統安全性。
