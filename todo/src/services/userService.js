/**
 * WEB API User Rest CRUD
 * ------------------------------------------------------------
 * POST   "http://localhost:8080/api/users/register"                     註冊用戶
 * POST   "http://localhost:8080/api/users/login"                        用戶登入
 * GET    "http://localhost:8080/api/users/{username}"                   根據用戶名查詢用戶資料
 * GET    "http://localhost:8080/api/users/exists/username/{username}"   檢查用戶名是否已存在
 * GET    "http://localhost:8080/api/users/exists/email/{email}"         檢查信箱是否已存在
 * ------------------------------------------------------------
 */

const BASE_URL = "http://localhost:8080/api/users";

// 統一的請求頭
const headers = {
  "Content-Type": "application/json",
  Accept: "application/json",
};

// 統一的錯誤處理
const handleResponse = async (response) => {
  if (!response.ok) {
    // 先嘗試解析錯誤訊息
    try {
      const errorData = await response.json();
      throw new Error(errorData.message || "操作失敗");
    } catch (e) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }
  const result = await response.json();
  if (result.status === 200) {
    return result.data;
  }
  throw new Error(result.message || "操作失敗");
};

// 用戶註冊
export const registerUser = async (userData) => {
  try {
    const response = await fetch(`${BASE_URL}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include", // 重要：允許發送 cookies
      body: JSON.stringify(userData),
    });
    const result = await response.json();
    if (result.status === 200) {
      return result.data;
    }
    throw new Error(result.message);
  } catch (error) {
    console.error("Registration error:", error);
    throw error;
  }
};

// 用戶登入
export const loginUser = async (userData) => {
  try {
    const response = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include", // 重要：允許發送 cookies
      body: JSON.stringify(userData),
    });
    const result = await response.json();
    if (result.status === 200) {
      return result.data;
    }
    throw new Error(result.message);
  } catch (error) {
    console.error("Login error:", error);
    throw error;
  }
};

// 檢查用戶名是否存在
export const checkUsername = async (username) => {
  try {
    const response = await fetch(`${BASE_URL}/exists/username/${username}`);
    return handleResponse(response);
  } catch (error) {
    console.error("Username check error:", error);
    throw error;
  }
};

// 檢查郵箱是否存在
export const checkEmail = async (email) => {
  try {
    const response = await fetch(`${BASE_URL}/exists/email/${email}`);
    return handleResponse(response);
  } catch (error) {
    console.error("Email check error:", error);
    throw error;
  }
};
// 登出
export const logoutUser = async () => {
  try {
    const response = await fetch(`${BASE_URL}/logout`, {
      method: "POST",
      credentials: "include", // 重要：允許發送 cookies
    });
    const result = await response.json();
    if (result.status === 200) {
      return true;
    }
    throw new Error(result.message);
  } catch (error) {
    console.error("Logout error:", error);
    throw error;
  }
};
// 請求密碼重設
export const requestPasswordReset = async (email) => {
  try {
    const response = await fetch(`${BASE_URL}/password-reset/request`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ email }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "重設密碼請求失敗");
    }

    const result = await response.json();
    if (result.status === 200) {
      return result.data;
    }
    throw new Error(result.message || "重設密碼請求失敗");
  } catch (error) {
    console.error("Password reset request error:", error);
    throw error;
  }
};

// 驗證重設令牌
export const validateResetToken = async (token) => {
  try {
    const response = await fetch(
      `${BASE_URL}/password-reset/validate?token=${token}`,
      {
        headers: {
          Accept: "application/json",
        },
        credentials: "include",
      }
    );
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "驗證令牌失敗");
    }
    return await response.json();
  } catch (error) {
    console.error("Token validation error:", error);
    throw error;
  }
};

// 重設密碼
export const resetPassword = async (token, newPassword) => {
  try {
    const response = await fetch(
      `${BASE_URL}/password-reset/reset?token=${token}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ password: newPassword }),
      }
    );
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "重設密碼失敗");
    }
    return await response.json();
  } catch (error) {
    console.error("Password reset error:", error);
    throw error;
  }
};
