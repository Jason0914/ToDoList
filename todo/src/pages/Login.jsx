import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../services/userService";
import { useAuth } from "../contexts/AuthContext";

/**
 * 登入頁面組件
 *
 * 提供用戶登入功能，包含用戶名和密碼輸入欄位。
 * 登入成功後將用戶重定向到待辦事項頁面。
 *
 * @returns {JSX.Element} 登入頁面
 */
function Login() {
  /**
   * 表單數據狀態
   * 包含用戶名和密碼欄位
   */
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  /**
   * 錯誤信息狀態
   * 用於顯示登入失敗的錯誤信息
   */
  const [error, setError] = useState(null);

  /**
   * 用於頁面導航的 hook
   */
  const navigate = useNavigate();

  /**
   * 從認證上下文獲取登入方法
   */
  const { login } = useAuth();

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
   * 執行登入操作並處理結果
   *
   * @param {Object} e - 事件對象
   */
  const handleSubmit = async (e) => {
    e.preventDefault(); // 阻止表單默認提交行為
    try {
      // 調用登入 API
      const userData = await loginUser(formData);
      // 更新認證上下文
      login(userData);
      // 重定向到待辦事項頁面
      navigate("/todo");
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
              <h2 className="text-center mb-4">登入</h2>

              {/* 錯誤信息顯示 */}
              {error && (
                <div className="alert alert-danger" role="alert">
                  {error}
                </div>
              )}

              {/* 登入表單 */}
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

                {/* 忘記密碼鏈接 */}
                <div className="mb-3 text-end">
                  <Link to="/forgot-password">忘記密碼？</Link>
                </div>

                {/* 提交按鈕 */}
                <button type="submit" className="btn btn-primary w-100">
                  登入
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
