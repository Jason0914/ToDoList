import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * 受保護路由組件
 *
 * 用於保護需要用戶登入才能訪問的路由。
 * 如果用戶未登入，自動重定向到登入頁面。
 *
 * @param {Object} props - 組件屬性
 * @param {React.ReactNode} props.children - 受保護的子組件
 * @returns {React.ReactNode} - 如果用戶已登入，返回子組件；否則重定向到登入頁面
 */
function ProtectedRoute({ children }) {
  // 從認證上下文獲取用戶和加載狀態
  const { user, loading } = useAuth();

  /**
   * 處理加載狀態
   *
   * 在確認用戶狀態前顯示載入指示器，
   * 避免在讀取 localStorage 時出現不必要的重定向。
   */
  if (loading) {
    return (
      <div className="text-center p-5">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-2">驗證用戶...</p>
      </div>
    );
  }

  /**
   * 檢查用戶是否已登入
   *
   * 如果未登入，重定向到登入頁面。
   * 使用 React Router 的 Navigate 組件實現無刷新重定向。
   */
  if (!user) {
    return <Navigate to="/login" />;
  }

  // 用戶已登入，渲染受保護的子組件
  return children;
}

export default ProtectedRoute;
