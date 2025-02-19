import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { validateResetToken, resetPassword } from "../services/userService";

function ResetPassword() {
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [isValidToken, setIsValidToken] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const navigate = useNavigate();
  const location = useLocation();
  const token = new URLSearchParams(location.search).get("token");

  useEffect(() => {
    if (!token) {
      setError("無效的重設連結");
      setIsLoading(false);
      return;
    }

    validateResetToken(token)
      .then(() => {
        setIsValidToken(true);
        setIsLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setIsValidToken(false);
        setIsLoading(false);
      });
  }, [token]);

  const validatePassword = (password) => {
    // 至少包含1個大寫字母、1個小寫字母、1個數字，且長度至少為8
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    return passwordRegex.test(password);
  };

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
      await resetPassword(token, password);
      setMessage("密碼重設成功！");
      // 3秒後重定向到登入頁面
      setTimeout(() => {
        navigate("/login");
      }, 3000);
    } catch (err) {
      setError(err.message);
    }
  };

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

  if (!isValidToken) {
    return (
      <div className="container mt-5">
        <div className="alert alert-danger">{error || "無效的重設連結"}</div>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">重設密碼</h2>
              {message && <div className="alert alert-success">{message}</div>}
              {error && <div className="alert alert-danger">{error}</div>}
              <form onSubmit={handleSubmit}>
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
