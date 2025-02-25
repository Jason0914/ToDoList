import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * 首頁組件
 *
 * 應用的歡迎頁面，根據用戶是否已登入顯示不同的內容。
 * 未登入用戶看到登入/註冊按鈕和應用特色介紹，
 * 已登入用戶看到功能入口按鈕。
 *
 * @returns {JSX.Element} 首頁
 */
function Home() {
  /**
   * 從認證上下文獲取登入狀態
   */
  const { isAuthenticated } = useAuth();

  return (
    <div className="fade-in">
      {/* 頁面標題區域 */}
      <div className="home-header">
        <h1>歡迎使用 Todo 應用</h1>
        <p className="lead">輕鬆管理您的待辦事項，提高工作效率</p>
      </div>

      {/* 未登入用戶內容 */}
      {!isAuthenticated && (
        <div className="container">
          {/* 登入/註冊按鈕區域 */}
          <div className="row justify-content-center g-4">
            <div className="col-md-6">
              <div className="card text-center">
                <div className="card-body">
                  <h5 className="card-title mb-4">準備開始使用？</h5>
                  <div className="d-grid gap-3">
                    <Link to="/login" className="btn btn-primary btn-lg">
                      登入
                    </Link>
                    <Link
                      to="/register"
                      className="btn btn-outline-primary btn-lg"
                    >
                      註冊新帳號
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* 應用特色介紹區域 */}
          <div className="row justify-content-center mt-5 g-4">
            {/* 特色 1：簡單易用 */}
            <div className="col-md-4">
              <div className="card h-100">
                <div className="card-body text-center">
                  <h3 className="card-title text-primary mb-3">簡單易用</h3>
                  <p className="card-text">
                    直覺的用戶界面，讓您輕鬆管理待辦事項，無需複雜操作。
                  </p>
                </div>
              </div>
            </div>

            {/* 特色 2：安全可靠 */}
            <div className="col-md-4">
              <div className="card h-100">
                <div className="card-body text-center">
                  <h3 className="card-title text-primary mb-3">安全可靠</h3>
                  <p className="card-text">
                    數據安全加密存儲，確保您的信息安全，隱私得到保護。
                  </p>
                </div>
              </div>
            </div>

            {/* 特色 3：隨時訪問 */}
            <div className="col-md-4">
              <div className="card h-100">
                <div className="card-body text-center">
                  <h3 className="card-title text-primary mb-3">隨時訪問</h3>
                  <p className="card-text">
                    支持多設備同步，讓您隨時隨地都能查看和管理待辦事項。
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* 已登入用戶內容 */}
      {isAuthenticated && (
        <div className="container text-center">
          <div className="card">
            <div className="card-body">
              <h2 className="card-title mb-4">開始管理您的待辦事項</h2>
              <Link to="/todo" className="btn btn-primary btn-lg">
                前往待辦事項清單
              </Link>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Home;
