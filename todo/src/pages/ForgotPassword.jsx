import React, { useState } from "react";
import { requestPasswordReset } from "../services/userService";
import { Link } from "react-router-dom";

/**
 * 忘記密碼頁面組件
 *
 * 提供用戶請求密碼重設的功能。
 * 用戶輸入電子郵件後，系統會發送重設密碼的連結至該郵箱。
 *
 * @returns {JSX.Element} 忘記密碼頁面
 */
function ForgotPassword() {
  /**
   * 電子郵件輸入狀態
   */
  const [email, setEmail] = useState("");

  /**
   * 成功信息狀態
   * 用於顯示請求成功的提示
   */
  const [message, setMessage] = useState("");

  /**
   * 錯誤信息狀態
   * 用於顯示請求失敗的錯誤
   */
  const [error, setError] = useState("");

  /**
   * 處理表單提交
   * 發送密碼重設請求
   *
   * @param {Object} e - 事件對象
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // 調用密碼重設 API
      await requestPasswordReset(email);

      // 顯示成功信息
      setMessage("密碼重設郵件已發送，請檢查您的信箱");

      // 清空錯誤信息和輸入框
      setError("");
      setEmail("");
    } catch (err) {
      // 顯示錯誤信息
      setError(err.message);
      setMessage("");
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">忘記密碼</h2>

              {/* 成功信息顯示 */}
              {message && <div className="alert alert-success">{message}</div>}

              {/* 錯誤信息顯示 */}
              {error && <div className="alert alert-danger">{error}</div>}

              {/* 密碼重設請求表單 */}
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">電子郵件</label>
                  <input
                    type="email"
                    className="form-control"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    placeholder="請輸入您註冊時使用的電子郵件"
                  />
                </div>

                {/* 提交按鈕 */}
                <button type="submit" className="btn btn-primary w-100 mb-3">
                  發送重設密碼郵件
                </button>

                {/* 返回登入頁鏈接 */}
                <div className="text-center">
                  <Link to="/login">返回登入</Link>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ForgotPassword;
