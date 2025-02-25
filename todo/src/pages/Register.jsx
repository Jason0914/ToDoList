import { useState } from "react";
import { registerUser } from "../services/userService";
import { useNavigate } from "react-router-dom";

/**
 * 註冊頁面組件
 *
 * 提供用戶註冊功能，包含用戶名、密碼和電子郵件輸入欄位。
 * 註冊成功後將用戶重定向到登入頁面。
 *
 * @returns {JSX.Element} 註冊頁面
 */
function Register() {
  /**
   * 表單數據狀態
   * 包含用戶名、密碼和電子郵件欄位
   */
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
  });

  /**
   * 錯誤信息狀態
   * 用於顯示註冊失敗的錯誤信息
   */
  const [error, setError] = useState(null);

  /**
   * 用於頁面導航的 hook
   */
  const navigate = useNavigate();

  /**
   * 處理表單欄位變更
   * 更新表單數據狀態
   *
   * @param {Object} e - 事件對象
   */
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  /**
   * 處理表單提交
   * 執行註冊操作並處理結果
   *
   * @param {Object} e - 事件對象
   */
  const handleSubmit = async (e) => {
    e.preventDefault(); // 阻止表單默認提交行為
    try {
      // 調用註冊 API
      await registerUser(formData);
      // 註冊成功後導向登入頁
      navigate("/login");
    } catch (err) {
      // 顯示錯誤信息
      setError(err.message);
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">註冊帳號</h2>

              {/* 錯誤信息顯示 */}
              {error && (
                <div className="alert alert-danger" role="alert">
                  {error}
                </div>
              )}

              {/* 註冊表單 */}
              <form onSubmit={handleSubmit}>
                {/* 用戶名輸入 */}
                <div className="mb-3">
                  <label className="form-label">用戶名</label>
                  <input
                    type="text"
                    name="username"
                    className="form-control"
                    value={formData.username}
                    onChange={handleChange}
                    required
                  />
                </div>

                {/* 密碼輸入 */}
                <div className="mb-3">
                  <label className="form-label">密碼</label>
                  <input
                    type="password"
                    name="password"
                    className="form-control"
                    value={formData.password}
                    onChange={handleChange}
                    required
                  />
                </div>

                {/* 電子郵件輸入 */}
                <div className="mb-3">
                  <label className="form-label">電子郵件</label>
                  <input
                    type="email"
                    name="email"
                    className="form-control"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>

                {/* 提交按鈕 */}
                <button type="submit" className="btn btn-primary w-100">
                  註冊
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;
