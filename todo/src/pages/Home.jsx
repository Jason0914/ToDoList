import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

function Home() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="fade-in">
      <div className="home-header">
        <h1>歡迎使用 Todo 應用</h1>
        <p className="lead">輕鬆管理您的待辦事項，提高工作效率</p>
      </div>

      {!isAuthenticated && (
        <div className="container">
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

          <div className="row justify-content-center mt-5 g-4">
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
