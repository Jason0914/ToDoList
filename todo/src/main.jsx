import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import "bootstrap/dist/css/bootstrap.min.css"; // 引入 Bootstrap 樣式
import "bootstrap/dist/js/bootstrap.bundle.min.js"; // 引入 Bootstrap JavaScript
import "./styles/custom.css"; // 自定義樣式覆蓋
import "./App.css";

/**
 * 應用入口點
 *
 * 負責將應用渲染到 DOM 中的根元素。
 * 使用 React.StrictMode 啟用嚴格模式，幫助檢測潛在問題。
 *
 * 這裡引入了以下樣式和腳本:
 * 1. Bootstrap CSS 和 JS - 提供響應式設計框架
 * 2. 自定義 CSS - 自定義 Bootstrap 的默認樣式
 * 3. App CSS - 應用特定的樣式
 */
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
