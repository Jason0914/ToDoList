/**
 * 用戶相關 API 服務
 *
 * 提供用戶註冊、登入、登出等功能的 API 調用方法。
 * 所有方法都返回 Promise，便於異步處理。
 *
 * 後端 API 端點:
 * ------------------------------------------------------------
 * POST   "http://localhost:8080/api/users/register"                     註冊用戶
 * POST   "http://localhost:8080/api/users/login"                        用戶登入
 * GET    "http://localhost:8080/api/users/{username}"                   根據用戶名查詢用戶資料
 * GET    "http://localhost:8080/api/users/exists/username/{username}"   檢查用戶名是否已存在
 * GET    "http://localhost:8080/api/users/exists/email/{email}"         檢查信箱是否已存在
 * ------------------------------------------------------------
 */

const BASE_URL = "http://localhost:8080/api/users";

/**
 * 請求標頭配置
 * 設置 Content-Type 和 Accept 為 JSON 格式
 */
const headers = {
  "Content-Type": "application/json",
  Accept: "application/json",
};

/**
 * 統一的響應處理器
 *
 * 處理 API 響應，包括錯誤情況。
 *
 * @param {Response} response - fetch API 的響應對象
 * @returns {Promise<any>} - 解析後的數據
 * @throws {Error} - 如果響應不成功或返回錯誤狀態碼
 */
const handleResponse = async (response) => {
  if (!response.ok) {
    // 嘗試解析錯誤信息
    try {
      const errorData = await response.json();
      throw new Error(errorData.message || "操作失敗");
    } catch (e) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  }
  // 解析響應數據
  const result = await response.json();
  if (result.status === 200) {
    return result.data;
  }
  throw new Error(result.message || "操作失敗");
};

/**
 * 用戶註冊
 *
 * @param {Object} userData - 用戶註冊數據
 * @param {string} userData.username - 用戶名
 * @param {string} userData.password - 密碼
 * @param {string} userData.email - 電子郵件
 * @returns {Promise<Object>} - 註冊成功的用戶數據
 * @throws {Error} - 註冊失敗時拋出錯誤
 */
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

/**
 * 用戶登入
 *
 * @param {Object} userData - 用戶登入數據
 * @param {string} userData.username - 用戶名
 * @param {string} userData.password - 密碼
 * @returns {Promise<Object>} - 登入成功的用戶數據
 * @throws {Error} - 登入失敗時拋出錯誤
 */
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

/**
 * 檢查用戶名是否存在
 *
 * @param {string} username - 要檢查的用戶名
 * @returns {Promise<boolean>} - 如果用戶名存在則返回 true
 * @throws {Error} - 請求失敗時拋出錯誤
 */
export const checkUsername = async (username) => {
  try {
    const response = await fetch(`${BASE_URL}/exists/username/${username}`);
    return handleResponse(response);
  } catch (error) {
    console.error("Username check error:", error);
    throw error;
  }
};

/**
 * 檢查郵箱是否存在
 *
 * @param {string} email - 要檢查的電子郵件
 * @returns {Promise<boolean>} - 如果電子郵件存在則返回 true
 * @throws {Error} - 請求失敗時拋出錯誤
 */
export const checkEmail = async (email) => {
  try {
    const response = await fetch(`${BASE_URL}/exists/email/${email}`);
    return handleResponse(response);
  } catch (error) {
    console.error("Email check error:", error);
    throw error;
  }
};

/**
 * 用戶登出
 *
 * @returns {Promise<boolean>} - 登出成功返回 true
 * @throws {Error} - 登出失敗時拋出錯誤
 */
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

/**
 * 請求密碼重設
 *
 * @param {string} email - 用戶電子郵件
 * @returns {Promise<Object>} - 請求結果
 * @throws {Error} - 請求失敗時拋出錯誤
 */
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

/**
 * 驗證重設令牌
 *
 * @param {string} token - 密碼重設令牌
 * @returns {Promise<Object>} - 驗證結果
 * @throws {Error} - 驗證失敗時拋出錯誤
 */
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

/**
 * 重設密碼
 *
 * @param {string} token - 密碼重設令牌
 * @param {string} newPassword - 新密碼
 * @returns {Promise<Object>} - 重設結果
 * @throws {Error} - 重設失敗時拋出錯誤
 */
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
