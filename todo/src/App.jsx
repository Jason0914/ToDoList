import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import TodoPage from "./pages/TodoPage";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import TransactionPage from "./pages/TransactionPage";
import { AuthProvider, useAuth } from "./contexts/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import "./App.css";

/**
 * 導航欄組件
 *
 * 顯示應用的頂部導航欄，根據用戶登入狀態動態調整顯示的選項。
 * 未登入用戶顯示登入/註冊選項，已登入用戶顯示功能頁面與登出選項。
 */
function NavBar() {
  // 從認證上下文獲取登入狀態、用戶信息和登出方法
  const { isAuthenticated, user, logout } = useAuth();

  /**
   * 處理登出操作
   * 調用 AuthContext 的登出方法，然後重定向到首頁
   */
  const handleLogout = () => {
    logout();
    window.location.href = "/";
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light mb-4">
      <div className="container">
        {/* 應用標題/Logo */}
        <Link className="navbar-brand" to="/">
          Todo
        </Link>

        {/* 響應式折疊按鈕 - 在小屏幕上顯示 */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            {/* 首頁鏈接 - 所有用戶可見 */}
            <li className="nav-item">
              <Link className="nav-link" to="/">
                首頁
              </Link>
            </li>

            {/* 根據登入狀態條件渲染不同的導航項目 */}
            {isAuthenticated ? (
              <>
                {/* 已登入用戶可見的功能鏈接 */}
                <li className="nav-item">
                  <Link className="nav-link" to="/todo">
                    待辦事項
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/transactions">
                    記帳
                  </Link>
                </li>
                <li className="nav-item">
                  <button
                    className="nav-link btn btn-link"
                    onClick={handleLogout}
                  >
                    登出
                  </button>
                </li>
              </>
            ) : (
              <>
                {/* 未登入用戶可見的認證鏈接 */}
                <li className="nav-item">
                  <Link className="nav-link" to="/login">
                    登入
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">
                    註冊
                  </Link>
                </li>
              </>
            )}
          </ul>

          {/* 顯示已登入用戶的用戶名 */}
          {isAuthenticated && user && (
            <span className="navbar-text">{user.username}</span>
          )}
        </div>
      </div>
    </nav>
  );
}

/**
 * 主應用組件
 *
 * 配置應用的路由系統並將整個應用包裝在認證提供者中，
 * 使所有子組件都能訪問用戶的認證狀態。
 */
function App() {
  return (
    <AuthProvider>
      <Router>
        <NavBar />
        <div className="container">
          <Routes>
            {/* 公開路由 - 所有用戶可訪問 */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/reset-password" element={<ResetPassword />} />

            {/* 受保護路由 - 僅登入用戶可訪問 */}
            <Route
              path="/todo"
              element={
                <ProtectedRoute>
                  <TodoPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/transactions"
              element={
                <ProtectedRoute>
                  <TransactionPage />
                </ProtectedRoute>
              }
            />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
