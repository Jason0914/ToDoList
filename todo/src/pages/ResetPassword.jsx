import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { validateResetToken, resetPassword } from "../services/userService";

/**
 * 重設密碼頁面組件
 *
 * 提供用戶重設密碼的功能。
 * 頁面會先驗證 URL 中的重設令牌，然後允許用戶設置新密碼。
 *
 * @returns {JSX.Element} 重設密碼頁面
 */
function ResetPassword() {
  /**
   * 新密碼輸入狀態
   */
  const [password, setPassword] = useState("");

  /**
   * 確認密碼輸入狀態
   */
  const [confirmPassword, setConfirmPassword] = useState("");

  /**
   * 成功信息狀態
   */
  const [message, setMessage] = useState("");

  /**
   * 錯誤信息狀態
   */
  const [error, setError] = useState("");

  /**
   * 令牌有效性狀態
   */
  const [isValidToken, setIsValidToken] = useState(false);

  /**
   * 加載狀態
   */
  const [isLoading, setIsLoading] = useState(true);

  /**
   * 用於頁面導航的 hook
   */
  const navigate = useNavigate();

  /**
   * 用於獲取 URL 參數的 hook
   */
  const location = useLocation();

  /**
   * 從 URL 中獲取令牌參數
   */
  const token = new URLSearchParams(location.search).get("token");

  /**
   * 頁面載入時驗證令牌
   */
  useEffect(() => {
    // 檢查令牌是否存在
    if (!token) {
      setError("無效的重設連結");
      setIsLoading(false);
      return;
    }

    // 驗證令牌有效性
    validateResetToken(token)
      .then(() => {
        // 令牌有效
        setIsValidToken(true);
        setIsLoading(false);
      })
      .catch((err) => {
        // 令牌無效或已過期
        setError(err.message);
        setIsValidToken(false);
        setIsLoading(false);
      });
  }, [token]);

  /**
   * 驗證密碼強度
   *
   * @param {string} password - 待驗證的密碼
   * @returns {boolean} 如果密碼符合規則則返回 true
   */
  const validatePassword = (password) => {
    // 至少包含1個大寫字母、1個小寫字母、1個數字，且長度至少為8
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    return passwordRegex.test(password);
  };

  /**
   * 處理表單提交
   * 執行密碼重設
   *
   * @param {Object} e - 事件對象
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    // 檢查密碼格式
    if (!validatePassword(password)) {
      setError("密碼必須至少包含8個字符，包括大寫字母、小寫字母和數字");
      return;
    }

    // 檢查密碼是否相同
    if (password !== confirmPassword) {
      setError("兩次輸入的密碼不相同");
      return;
    }

    try {
      // 調用重設密碼 API
      await resetPassword(token, password);

      // 顯示成功信息
      setMessage("密碼重設成功！");

      // 3秒後重定向到登入頁面
      setTimeout(() => {
        navigate("/login");
      }, 3000);
    } catch (err) {
      // 顯示錯誤信息
      setError(err.message);
    }
  };

  // 顯示加載狀態
  if (isLoading) {
    return (
      <div className="container mt-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      </div>
    );
  }

  // 顯示令牌無效錯誤
  if (!isValidToken) {
    return (
      <div className="container mt-5">
        <div className="alert alert-danger">{error || "無效的重設連結"}</div>
      </div>
    );
  }

  // 顯示重設密碼表單
  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">重設密碼</h2>

              {/* 成功信息顯示 */}
              {message && <div className="alert alert-success">{message}</div>}

              {/* 錯誤信息顯示 */}
              {error && <div className="alert alert-danger">{error}</div>}

              {/* 重設密碼表單 */}
              <form onSubmit={handleSubmit}>
                {/* 新密碼輸入 */}
                <div className="mb-3">
                  <label className="form-label">新密碼</label>
                  <input
                    type="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                  <small className="text-muted">
                    密碼必須至少包含8個字符，包括大寫字母、小寫字母和數字
                  </small>
                </div>

                {/* 確認密碼輸入 */}
                <div className="mb-3">
                  <label className="form-label">確認新密碼</label>
                  <input
                    type="password"
                    className="form-control"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                  />
                </div>

                {/* 提交按鈕 */}
                <button type="submit" className="btn btn-primary w-100">
                  重設密碼
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ResetPassword;
