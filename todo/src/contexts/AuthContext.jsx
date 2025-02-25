import { createContext, useState, useContext, useEffect } from "react";

/**
 * 創建認證上下文
 *
 * 使用 React Context API 創建一個全局的認證狀態管理器。
 * 初始值為 null，表示尚未初始化。
 */
const AuthContext = createContext(null);

/**
 * 認證提供者組件
 *
 * 包裝整個應用，提供認證狀態和相關方法給所有子組件。
 * 實現了用戶登入狀態的持久化(通過 localStorage)和共享。
 *
 * @param {Object} props - 組件屬性
 * @param {React.ReactNode} props.children - 子組件
 */
export function AuthProvider({ children }) {
  // 用戶認證狀態
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  // 用戶信息
  const [user, setUser] = useState(null);
  // 加載狀態，用於防止在讀取 localStorage 時出現閃爍
  const [loading, setLoading] = useState(true);

  /**
   * 初始化效果
   *
   * 在組件掛載時從 localStorage 讀取用戶狀態，
   * 實現頁面刷新後保留用戶登入狀態的功能。
   */
  useEffect(() => {
    // 從 localStorage 檢查用戶狀態
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      try {
        // 嘗試解析存儲的用戶數據
        const userData = JSON.parse(storedUser);
        setUser(userData);
        setIsAuthenticated(true);
      } catch (error) {
        // 如果數據損壞，清除它
        console.error("Error parsing stored user data:", error);
        localStorage.removeItem("user");
      }
    }
    // 設置加載完成
    setLoading(false);
  }, []);

  /**
   * 登入方法
   *
   * 將用戶數據存儲到 localStorage 並更新狀態。
   *
   * @param {Object} userData - 用戶信息對象
   */
  const login = (userData) => {
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
    setIsAuthenticated(true);
  };

  /**
   * 登出方法
   *
   * 清除 localStorage 中的用戶數據並重置狀態。
   */
  const logout = () => {
    localStorage.removeItem("user");
    setUser(null);
    setIsAuthenticated(false);
  };

  // 提供認證狀態和方法給所有子組件
  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        user,
        login,
        logout,
        setIsAuthenticated,
        loading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

/**
 * 使用認證上下文的自定義 Hook
 *
 * 提供一個簡潔的方式來在組件中訪問認證狀態和方法。
 *
 * @returns {Object} 認證上下文的值
 * @throws {Error} 如果在 AuthProvider 外部使用則拋出錯誤
 */
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
